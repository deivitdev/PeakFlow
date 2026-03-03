package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.*
import com.deivitdev.peakflow.domain.repository.ActivityRepository
import kotlinx.datetime.*

class GetFitnessFatigueUseCase(private val repository: ActivityRepository) {
    suspend operator fun invoke(days: Int = 90, futureTss: Int? = null): FitnessFatigueData {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val startDate = now.minus(DatePeriod(days = days + 42)) // 42 days extra to "warm up" CTL
        
        val dailyLoads = repository.getDailyLoads(startDate.toString(), now.toString())
        val loadMap = dailyLoads.associate { it.date to it.tss }

        val points = mutableListOf<FitnessFatiguePoint>()
        var currentCtl = 0f
        var currentAtl = 0f

        var date = startDate
        while (date <= now) {
            val tss = loadMap[date.toString()] ?: 0f
            
            // Formula: CTL_n = CTL_n-1 + (TSS_n - CTL_n-1) / 42
            // Formula: ATL_n = ATL_n-1 + (TSS_n - ATL_n-1) / 7
            
            val nextCtl = currentCtl + (tss - currentCtl) / 42f
            val nextAtl = currentAtl + (tss - currentAtl) / 7f
            
            // Resulting Form: TSB after today's load
            val form = nextCtl - nextAtl 

            if (date > startDate.plus(DatePeriod(days = 42))) {
                points.add(
                    FitnessFatiguePoint(
                        date = date.toString(),
                        fitness = nextCtl,
                        fatigue = nextAtl,
                        form = form
                    )
                )
            }

            currentCtl = nextCtl
            currentAtl = nextAtl
            date = date.plus(DatePeriod(days = 1))
        }

        val latestForm = currentCtl - currentAtl

        // 2-Day Projection
        val projection = if (futureTss != null) {
            val projPoints = mutableListOf<FitnessFatiguePoint>()
            var pCtl = currentCtl
            var pAtl = currentAtl
            var pDate = now
            
            repeat(2) { i ->
                pDate = pDate.plus(DatePeriod(days = 1))
                val tss = if (i == 0) futureTss.toFloat() else 0f // Assume 0 after the selected path
                val nCtl = pCtl + (tss - pCtl) / 42f
                val nAtl = pAtl + (tss - pAtl) / 7f
                projPoints.add(FitnessFatiguePoint(pDate.toString(), nCtl, nAtl, nCtl - nAtl))
                pCtl = nCtl
                pAtl = nAtl
            }
            projPoints
        } else null

        // Calculate Ramp Rate (Weekly CTL increase)
        val rampRateData = if (points.size >= 7) {
            val currentFitness = currentCtl
            val fitness7DaysAgo = points[points.size - 7].fitness
            val increase = currentFitness - fitness7DaysAgo
            
            val rampStatus = when {
                increase < 2f -> RampRateStatus.MAINTENANCE
                increase < 5f -> RampRateStatus.PRODUCTIVE
                increase < 8f -> RampRateStatus.INTENSE
                else -> RampRateStatus.RISKY
            }
            RampRateData(increase, rampStatus)
        } else null

        val status = when {
            points.size < 21 -> TrainingStatus.BUILDING_DATA
            latestForm > 5f -> TrainingStatus.FRESH
            latestForm < -30f -> TrainingStatus.OVERREACHING
            latestForm < -10f -> TrainingStatus.OPTIMAL
            latestForm > 0f -> TrainingStatus.RECOVERY
            else -> TrainingStatus.NEUTRAL
        }

        return FitnessFatigueData(
            history = points.takeLast(days),
            currentStatus = status,
            currentFitness = currentCtl,
            currentFatigue = currentAtl,
            currentForm = latestForm,
            projection = projection,
            rampRate = rampRateData
        )
    }
}
