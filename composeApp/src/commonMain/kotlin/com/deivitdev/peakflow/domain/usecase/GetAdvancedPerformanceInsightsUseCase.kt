package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.AggregatedZones
import com.deivitdev.peakflow.domain.model.MetabolicSummary
import com.deivitdev.peakflow.domain.model.PolarizationSummary
import com.deivitdev.peakflow.domain.repository.ActivityRepository
import com.deivitdev.peakflow.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.*

data class AdvancedPerformanceInsights(
    val last7DaysPolarization: PolarizationSummary,
    val last28DaysPolarization: PolarizationSummary,
    val recentMetabolicSummary: MetabolicSummary,
    val hydrationAmountLiters: Float? = null,
    val greyZonePercentage: Int = 0
)

class GetAdvancedPerformanceInsightsUseCase(
    private val activityRepository: ActivityRepository,
    private val userRepository: UserRepository,
    private val calculateMetabolic: CalculateMetabolicOxidationUseCase,
    private val calculatePolarization: CalculatePolarizationRatioUseCase
) {
    suspend operator fun invoke(): AdvancedPerformanceInsights {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val last7DaysStart = now.minus(7, DateTimeUnit.DAY).toString()
        val last28DaysStart = now.minus(28, DateTimeUnit.DAY).toString()
        val today = now.toString()

        val activities = activityRepository.getActivities()
        
        // Hydrate the last 5 activities to ensure we have some data for the charts
        activities.take(5).forEach { summary ->
            activityRepository.getActivity(summary.id)
        }

        // Re-fetch aggregated zones now that we've hydrated recent activities
        val zones7 = activityRepository.getAggregatedZones(last7DaysStart, today)
        val zones28 = activityRepository.getAggregatedZones(last28DaysStart, today)

        val polarization7 = calculatePolarization(zones7)
        val polarization28 = calculatePolarization(zones28)

        val lastActivity = activities.firstOrNull()?.let { activityRepository.getActivity(it.id) }
        
        val metabolicSummary = lastActivity?.let { calculateMetabolic(it) } 
            ?: MetabolicSummary(0f, 0f, 0)

        // Calculate dynamic hydration recommendation
        val profile = userRepository.getUserProfile().firstOrNull()
        val hydrationAmount = lastActivity?.let { activity ->
            val ftp = profile?.ftpWatts?.toFloat() ?: 200f
            val intensityFactor = if (activity.weightedAveragePower != null) {
                activity.weightedAveragePower / ftp
            } else {
                activity.averagePower?.let { it / ftp } ?: 0.6f
            }

            val temp = activity.averageTemp ?: 20 // Default to 20 if missing
            val baseRate = 0.6f // Liters per hour baseline
            val durationHrs = activity.movingTimeSeconds / 3600f
            
            // Heat factor: increases recommendation if it's hot
            val heatFactor = when {
                temp > 30 -> 1.5f
                temp > 25 -> 1.25f
                else -> 1.0f
            }
            
            // Intensity factor: more intensity = more sweat
            val adjustedIntensity = intensityFactor.coerceIn(0.5f, 1.2f)
            
            (durationHrs * baseRate * adjustedIntensity * heatFactor)
        }

        return AdvancedPerformanceInsights(
            last7DaysPolarization = polarization7,
            last28DaysPolarization = polarization28,
            recentMetabolicSummary = metabolicSummary,
            hydrationAmountLiters = hydrationAmount,
            greyZonePercentage = polarization28.thresholdIntensityPercentage.toInt()
        )
    }
}
