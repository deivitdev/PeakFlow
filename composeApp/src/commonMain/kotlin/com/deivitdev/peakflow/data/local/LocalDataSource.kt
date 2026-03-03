package com.deivitdev.peakflow.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.deivitdev.peakflow.db.Activity
import com.deivitdev.peakflow.db.PeakFlowDatabase
import com.deivitdev.peakflow.db.Token
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalDataSource(val database: PeakFlowDatabase) {
    private val queries = database.peakFlowDatabaseQueries

    suspend fun insertActivity(activity: Activity) = withContext(Dispatchers.IO) {
        queries.insertActivity(
            activity.id,
            activity.name,
            activity.type,
            activity.distance,
            activity.movingTime,
            activity.elapsedTime,
            activity.totalElevationGain,
            activity.startDate,
            activity.averageSpeed,
            activity.maxSpeed,
            activity.averageHeartRate,
            activity.maxHeartRate,
            activity.calories,
            activity.kilojoules,
            activity.sufferScore,
            activity.averagePower,
            activity.location,
            activity.polyline,
            activity.weightedAverageWatts,
            activity.maxWatts,
            activity.deviceWatts,
            activity.averageTemp,
            activity.elevHigh,
            activity.elevLow,
            activity.deviceName,
            activity.gearName,
            activity.gearDistance,
            activity.splitsJson,
            activity.bestEffortsJson,
            activity.streams,
            activity.z1Seconds,
            activity.z2Seconds,
            activity.z3Seconds,
            activity.z4Seconds,
            activity.z5Seconds
        )
    }

    suspend fun getSummedZonesInRange(start: String, end: String) = withContext(Dispatchers.IO) {
        queries.selectSummedZonesInRange(start, end).executeAsOne()
    }

    suspend fun getAllActivities(): List<Activity> = withContext(Dispatchers.IO) {
        queries.selectAllActivities().executeAsList()
    }

    suspend fun getLatestActivityDate(): String? = withContext(Dispatchers.IO) {
        queries.getLatestActivityDate().executeAsOneOrNull()
    }

    fun getAllActivitiesFlow(): Flow<List<Activity>> {
        return queries.selectAllActivities().asFlow().mapToList(Dispatchers.IO)
    }

    suspend fun getActivityById(id: String): Activity? = withContext(Dispatchers.IO) {
        queries.getActivityById(id).executeAsOneOrNull()
    }

    suspend fun insertToken(token: Token) = withContext(Dispatchers.IO) {
        queries.insertToken(
            id = token.id,
            accessToken = token.accessToken,
            refreshToken = token.refreshToken,
            expiresAt = token.expiresAt
        )
    }

    suspend fun getToken(id: String): Token? = withContext(Dispatchers.IO) {
        queries.getToken(id).executeAsOneOrNull()
    }

    fun getTokenFlow(id: String): Flow<Token?> {
        return queries.getToken(id).asFlow().mapToOneOrNull(Dispatchers.IO)
    }

    suspend fun deleteToken(id: String) = withContext(Dispatchers.IO) {
        queries.deleteToken(id)
    }

    suspend fun insertDailyLoad(date: String, tss: Double) = withContext(Dispatchers.IO) {
        queries.upsertDailyLoad(date, tss)
    }

    suspend fun getDailyLoadsInRange(start: String, end: String) = withContext(Dispatchers.IO) {
        queries.getDailyLoadsInRange(start, end).executeAsList()
    }

    fun getDailyLoadsInRangeFlow(start: String, end: String): Flow<List<com.deivitdev.peakflow.db.DailyLoad>> {
        return queries.getDailyLoadsInRange(start, end).asFlow().mapToList(Dispatchers.IO)
    }

    suspend fun clearDailyLoads() = withContext(Dispatchers.IO) {
        queries.clearDailyLoads()
    }
}
