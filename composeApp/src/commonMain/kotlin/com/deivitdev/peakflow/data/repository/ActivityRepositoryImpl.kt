package com.deivitdev.peakflow.data.repository

import com.deivitdev.peakflow.data.local.LocalDataSource
import com.deivitdev.peakflow.data.mapper.toDb
import com.deivitdev.peakflow.data.mapper.toDomain
import com.deivitdev.peakflow.data.remote.StravaApiClient
import com.deivitdev.peakflow.data.remote.model.ActivityStreams
import com.deivitdev.peakflow.data.remote.model.StravaAthleteDto
import com.deivitdev.peakflow.db.Token
import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.model.WorkoutType
import com.deivitdev.peakflow.domain.model.StravaConfig
import com.deivitdev.peakflow.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.floatOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonPrimitive

class ActivityRepositoryImpl(
    private val apiClient: StravaApiClient,
    private val localDataSource: LocalDataSource,
    private val config: StravaConfig
) : ActivityRepository {

    private var lastSyncTimestamp: Long = 0

    private suspend fun getValidToken(): Token? {
        val token = localDataSource.getToken("current_user") ?: run {
            println("STRAVA_AUTH: No token found in database.")
            return null
        }
        val currentEpoch = Clock.System.now().epochSeconds
        
        // Refresh token 5 minutes before expiration
        if (token.expiresAt < currentEpoch + 300) {
            println("STRAVA_AUTH: Token expiring in ${token.expiresAt - currentEpoch}s. Refreshing...")
            try {
                val newTokenDto = apiClient.refreshAccessToken(token.refreshToken, config.clientId, config.clientSecret)
                val newToken = Token(
                    id = "current_user",
                    accessToken = newTokenDto.accessToken,
                    refreshToken = newTokenDto.refreshToken,
                    expiresAt = newTokenDto.expiresAt
                )
                localDataSource.insertToken(newToken)
                println("STRAVA_AUTH: Token refreshed successfully. New expiry: ${newToken.expiresAt}")
                return newToken
            } catch (e: Exception) {
                println("STRAVA_AUTH_ERROR: Failed to refresh token: ${e.message}")
                return null
            }
        }
        println("STRAVA_AUTH: Using existing valid token (expires in ${token.expiresAt - currentEpoch}s).")
        return token
    }

    override suspend fun syncActivities(): Result<Unit> = runCatching {
        val currentEpoch = Clock.System.now().epochSeconds
        // Throttling: Prevent syncing more than once every 60 seconds
        if (currentEpoch - lastSyncTimestamp < 60) {
            println("STRAVA_SYNC: Skipping sync (already synced less than 60s ago).")
            return@runCatching
        }
        
        println("STRAVA_SYNC: Starting sync...")
        val token = getValidToken() 
            ?: run {
                println("STRAVA_SYNC_ERROR: No valid token found.")
                throw IllegalStateException("Not connected to Strava")
            }
        
        val latestDateStr = localDataSource.getLatestActivityDate()
        println("STRAVA_SYNC: Latest activity in local DB: $latestDateStr")
        
        val afterTimestamp = latestDateStr?.let {
            try {
                Instant.parse(it).epochSeconds
            } catch (e: Exception) {
                println("STRAVA_SYNC_ERROR: Failed to parse date $it: ${e.message}")
                null
            }
        }

        println("STRAVA_SYNC: Fetching activities from API ${afterTimestamp?.let { "(after $latestDateStr)" } ?: "from beginning"}...")
        val remoteActivities = apiClient.getActivities(
            accessToken = token.accessToken, 
            perPage = 100,
            after = afterTimestamp
        )
        
        println("STRAVA_SYNC: Received ${remoteActivities.size} new activities.")
        remoteActivities.forEach { dto ->
            localDataSource.insertActivity(dto.toDb())
        }
        
        lastSyncTimestamp = currentEpoch
        println("STRAVA_SYNC: All activities saved to DB. Next sync allowed in 60s.")
    }

    override suspend fun getActivities(): List<Activity> {
        return localDataSource.getAllActivities().map { it.toDomain() }
    }

    override fun getActivitiesFlow(): Flow<List<Activity>> {
        return localDataSource.getAllActivitiesFlow().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun getActivity(id: String): Activity? {
        val dbActivity = localDataSource.getActivityById(id) ?: return null
        
        // If we are missing the new advanced data (splits or power for cycling), fetch it again
        val isMissingAdvancedData = dbActivity.splitsJson == null || 
                (dbActivity.type.lowercase().contains("ride") && dbActivity.weightedAverageWatts == null)
        
        val streams = dbActivity.streams?.let { Json.decodeFromString<ActivityStreams>(it) }
        val isMissingNewStreams = dbActivity.streams != null && (streams?.velocitySmooth == null || (dbActivity.type.lowercase().contains("ride") && streams?.watts == null))

        if (dbActivity.streams == null || isMissingAdvancedData || isMissingNewStreams) {
            println("FETCHING_REAL_DATA: Activity $id needs update. Fetching from Strava...")
            val token = getValidToken() ?: return dbActivity.toDomain()
            
            try {
                // Fetch full detail and streams
                val detailDto = apiClient.getActivityDetail(token.accessToken, id)
                val streamsMap = apiClient.getStreams(token.accessToken, id)
                
                println("STRAVA_API: Received streams keys: ${streamsMap.keys} for activity $id")
                
                val hrData = streamsMap["heartrate"]?.data?.mapNotNull { it.jsonPrimitive.intOrNull }
                val altData = streamsMap["altitude"]?.data?.mapNotNull { it.jsonPrimitive.floatOrNull }
                val cadData = streamsMap["cadence"]?.data?.mapNotNull { it.jsonPrimitive.intOrNull }
                val velData = streamsMap["velocity_smooth"]?.data?.mapNotNull { it.jsonPrimitive.floatOrNull }
                val wattsData = streamsMap["watts"]?.data?.mapNotNull { it.jsonPrimitive.intOrNull }
                
                if (hrData == null && altData == null) {
                    println("STRAVA_API_WARNING: No heartrate or altitude streams found in response keys.")
                }

                // Fetch user profile to get custom HR Max
                val profile = localDataSource.database.peakFlowDatabaseQueries.getUserProfile().executeAsOneOrNull()
                val hrMax = profile?.hrMax?.toInt()?.takeIf { it > 0 } ?: 190

                val zones = calculateHrZones(hrData ?: emptyList(), hrMax)
                val streamsBlob = Json.encodeToString(
                    ActivityStreams(
                        heartRate = hrData, 
                        elevation = altData, 
                        cadence = cadData,
                        velocitySmooth = velData,
                        watts = wattsData
                    )
                )
                
                // Update DB with full detail and streams
                val updatedDbActivity = detailDto.toDb().copy(
                    streams = streamsBlob,
                    z1Seconds = zones[1],
                    z2Seconds = zones[2],
                    z3Seconds = zones[3],
                    z4Seconds = zones[4],
                    z5Seconds = zones[5]
                )
                localDataSource.insertActivity(updatedDbActivity)
                
                println("SUCCESS: Real streams persisted for activity $id. Points: HR(${hrData?.size ?: 0}), ALT(${altData?.size ?: 0}), CAD(${cadData?.size ?: 0})")
                return updatedDbActivity.toDomain()
            } catch (e: Exception) {
                println("ERROR_FETCHING_STRAVA_DETAILS: ${e.message}")
                e.printStackTrace()
                return dbActivity.toDomain()
            }
        }
        
        return dbActivity.toDomain()
    }

    private fun calculateHrZones(hrData: List<Int>, hrMax: Int): Map<Int, Long> {
        val zones = mutableMapOf(1 to 0L, 2 to 0L, 3 to 0L, 4 to 0L, 5 to 0L)
        hrData.forEach { hr ->
            val pct = (hr.toFloat() / hrMax) * 100f
            when {
                pct < 60f -> zones[1] = zones[1]!! + 1
                pct < 70f -> zones[2] = zones[2]!! + 1
                pct < 80f -> zones[3] = zones[3]!! + 1
                pct < 90f -> zones[4] = zones[4]!! + 1
                else -> zones[5] = zones[5]!! + 1
            }
        }
        return zones
    }

    override suspend fun connect(code: String): Result<Unit> = runCatching {
        println("STRAVA_AUTH: Getting access token for code $code...")
        val tokenDto = apiClient.getAccessToken(code, config.clientId, config.clientSecret, config.redirectUri)
        println("STRAVA_AUTH: Received token for user.")
        localDataSource.insertToken(
            Token(
                id = "current_user",
                accessToken = tokenDto.accessToken,
                refreshToken = tokenDto.refreshToken,
                expiresAt = tokenDto.expiresAt
            )
        )
        println("STRAVA_AUTH: Token saved to database.")
    }

    override suspend fun disconnect(): Result<Unit> = runCatching {
        println("STRAVA_AUTH: Disconnecting from Strava (deleting local token).")
        localDataSource.deleteToken("current_user")
    }

    override suspend fun isConnected(): Boolean {
        return localDataSource.getToken("current_user") != null
    }

    override fun connectionStatusFlow(): Flow<Boolean> {
        return localDataSource.getTokenFlow("current_user").map { it != null }
    }

    override suspend fun getAuthenticatedAthlete(): Result<StravaAthleteDto> = runCatching {
        val token = getValidToken() 
            ?: throw IllegalStateException("Not connected to Strava")
        apiClient.getAuthenticatedAthlete(token.accessToken)
    }

    override suspend fun getAggregatedZones(startDate: String, endDate: String): com.deivitdev.peakflow.domain.model.AggregatedZones {
        val result = localDataSource.getSummedZonesInRange(startDate, endDate)
        return com.deivitdev.peakflow.domain.model.AggregatedZones(
            z1Seconds = result.z1?.toLong() ?: 0L,
            z2Seconds = result.z2?.toLong() ?: 0L,
            z3Seconds = result.z3?.toLong() ?: 0L,
            z4Seconds = result.z4?.toLong() ?: 0L,
            z5Seconds = result.z5?.toLong() ?: 0L
        )
    }

    override suspend fun getDailyLoads(startDate: String, endDate: String): List<com.deivitdev.peakflow.domain.model.DailyLoad> {
        return localDataSource.getDailyLoadsInRange(startDate, endDate).map {
            com.deivitdev.peakflow.domain.model.DailyLoad(it.date, it.tss.toFloat())
        }
    }

    override fun getDailyLoadsFlow(startDate: String, endDate: String): Flow<List<com.deivitdev.peakflow.domain.model.DailyLoad>> {
        return localDataSource.getDailyLoadsInRangeFlow(startDate, endDate).map { list ->
            list.map { com.deivitdev.peakflow.domain.model.DailyLoad(it.date, it.tss.toFloat()) }
        }
    }

    override suspend fun saveDailyLoad(date: String, tss: Float) {
        localDataSource.insertDailyLoad(date, tss.toDouble())
    }

    override suspend fun clearDailyLoads() {
        localDataSource.clearDailyLoads()
    }
}
