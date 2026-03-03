package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.*
import com.deivitdev.peakflow.domain.repository.ActivityRepository
import com.deivitdev.peakflow.data.remote.model.StravaAthleteDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

// Simple manual wrapper since kotlinx-coroutines-test is missing
fun runTestBlocking(block: suspend () -> Unit) {
    kotlinx.coroutines.runBlocking { block() }
}

class RampRateTest {

    @Test
    fun testRampRateCalculation() = runTestBlocking {
        val repository = object : ActivityRepository {
            override suspend fun syncActivities() = Result.success(Unit)
            override suspend fun getActivities() = emptyList<Activity>()
            override fun getActivitiesFlow() = flowOf(emptyList<Activity>())
            override suspend fun getActivity(id: String) = null
            override suspend fun connect(code: String) = Result.success(Unit)
            override suspend fun disconnect() = Result.success(Unit)
            override suspend fun isConnected() = true
            override fun connectionStatusFlow(): Flow<Boolean> {
                TODO("Not yet implemented")
            }

            override suspend fun getAuthenticatedAthlete(): Result<StravaAthleteDto> = Result.failure(Exception())
            override suspend fun getAggregatedZones(start: String, end: String) = AggregatedZones(0,0,0,0,0)
            
            override suspend fun getDailyLoads(start: String, end: String): List<DailyLoad> {
                val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                val loads = mutableListOf<DailyLoad>()
                // Create a steady increase (100 TSS daily)
                for (i in 0..100) {
                    val date = now.minus(DatePeriod(days = i))
                    loads.add(DailyLoad(date.toString(), 100f))
                }
                return loads
            }
            override fun getDailyLoadsFlow(start: String, end: String) = flowOf(emptyList<DailyLoad>())
            override suspend fun saveDailyLoad(date: String, tss: Float) {}
            override suspend fun clearDailyLoads() {}
        }

        val useCase = GetFitnessFatigueUseCase(repository)
        val result = useCase(days = 30)

        assertNotNull(result.rampRate)
        assertTrue(result.rampRate!!.weeklyIncrease > 0, "Ramp rate should be positive")
        
        // With 100 TSS daily, CTL should be increasing
        // Status should be PRODUCTIVE or INTENSE (since it's a steep start from 0)
        assertTrue(result.rampRate!!.status != RampRateStatus.MAINTENANCE)
    }
}
