package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.model.AerobicStatus
import com.deivitdev.peakflow.domain.model.UserProfile
import com.deivitdev.peakflow.domain.model.WorkoutType

data class PerformanceMetrics(
    val ifValue: Float? = null, // Intensity Factor
    val tssValue: Float? = null, // Training Stress Score
    val decouplingPercentage: Float? = null,
    val aerobicStatus: AerobicStatus = AerobicStatus.INSUFFICIENT_DATA,
    val avgWkg: Float? = null,
    val maxWkg: Float? = null,
    val avgGapKmh: Float? = null
)

class CalculatePerformanceMetricsUseCase {
    operator fun invoke(activity: Activity, userProfile: UserProfile?): PerformanceMetrics {
        val weightedAveragePower = activity.weightedAveragePower
        val ftpWatts = userProfile?.ftpWatts?.toFloat()
        val movingTimeHours = activity.movingTimeSeconds / 3600f
        val weightKg = userProfile?.weightKg
        
        val decoupling = calculateDecoupling(activity)
        
        val avgWkg = if (weightKg != null && weightKg > 0 && activity.averagePower != null) {
            activity.averagePower / weightKg
        } else null
        
        val maxWkg = if (weightKg != null && weightKg > 0 && activity.maxPower != null) {
            activity.maxPower / weightKg
        } else null

        val avgGapKmh = activity.gapSeries?.filter { it > 0 }?.average()?.toFloat()?.let { it * 3.6f }

        if (weightedAveragePower == null || ftpWatts == null || ftpWatts == 0f || movingTimeHours == 0f) {
            // Fallback to sufferScore if power is missing
            val fallbackTss = activity.sufferScore?.toFloat()
            return PerformanceMetrics(
                tssValue = fallbackTss,
                decouplingPercentage = decoupling.first,
                aerobicStatus = decoupling.second,
                avgWkg = avgWkg,
                maxWkg = maxWkg,
                avgGapKmh = avgGapKmh
            )
        }

        val ifValue = weightedAveragePower / ftpWatts
        val correctedTss = movingTimeHours * ifValue * ifValue * 100f

        return PerformanceMetrics(
            ifValue = ifValue,
            tssValue = correctedTss,
            decouplingPercentage = decoupling.first,
            aerobicStatus = decoupling.second,
            avgWkg = avgWkg,
            maxWkg = maxWkg,
            avgGapKmh = avgGapKmh
        )
    }

    private fun calculateDecoupling(activity: Activity): Pair<Float?, AerobicStatus> {
        val hrSeries = activity.heartRateSeries
        val effortSeries = when (activity.type) {
            is WorkoutType.CYCLING -> activity.wattsSeries?.map { it.toFloat() }
            is WorkoutType.WALKING -> activity.velocitySmoothSeries
            else -> null
        }

        if (hrSeries == null || effortSeries == null || hrSeries.size < 100 || hrSeries.size != effortSeries.size) {
            return null to AerobicStatus.INSUFFICIENT_DATA
        }

        // Split into two halves
        val midpoint = hrSeries.size / 2
        
        val hr1 = hrSeries.subList(0, midpoint)
        val effort1 = effortSeries.subList(0, midpoint)
        
        val hr2 = hrSeries.subList(midpoint, hrSeries.size)
        val effort2 = effortSeries.subList(midpoint, effortSeries.size)

        // Calculate Efficiency Factor (EF) = Output / Heart Rate
        // For walking, velocity is in m/s from Strava usually? 
        // Actually activity.velocitySmoothSeries is from Strava.
        
        val avgHr1 = hr1.average().toFloat()
        val avgEffort1 = effort1.average().toFloat()
        
        val avgHr2 = hr2.average().toFloat()
        val avgEffort2 = effort2.average().toFloat()

        if (avgHr1 == 0f || avgHr2 == 0f || avgEffort1 <= 0f || avgEffort2 <= 0f) {
            return null to AerobicStatus.INSUFFICIENT_DATA
        }

        val ef1 = avgEffort1 / avgHr1
        val ef2 = avgEffort2 / avgHr2

        // Decoupling = ((EF1 - EF2) / EF1) * 100
        val decoupling = ((ef1 - ef2) / ef1) * 100f
        
        val status = if (decoupling < 5f) AerobicStatus.STABLE else AerobicStatus.DRIFT_DETECTED
        
        return decoupling to status
    }
}
