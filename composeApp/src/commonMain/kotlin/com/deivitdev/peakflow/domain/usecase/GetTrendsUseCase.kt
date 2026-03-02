package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.repository.ActivityRepository
import kotlinx.datetime.*

enum class TrendMetric {
    DISTANCE,
    DURATION,
    ELEVATION
}

enum class TrendGranularity {
    WEEKLY,
    MONTHLY
}

data class TrendDataPoint(
    val date: LocalDate, // Represents the start of the week/month
    val value: Float
)

class GetTrendsUseCase(private val repository: ActivityRepository) {

    suspend operator fun invoke(
        metric: TrendMetric,
        granularity: TrendGranularity,
        numPeriods: Int = 12 // Default to last 12 weeks/months
    ): List<TrendDataPoint> {
        val allActivities = repository.getActivities()
        if (allActivities.isEmpty()) return emptyList()


// ... (rest of the file) ...

        val trendsMap = mutableMapOf<LocalDate, Float>()
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Initialize trendsMap with 0s for the last numPeriods
        for (i in 0 until numPeriods) {
            val periodDate = when (granularity) {
                TrendGranularity.WEEKLY -> {
                    val currentWeek = now.date.minus(kotlinx.datetime.DatePeriod(days = i * 7))
                    val dayOfWeek = currentWeek.dayOfWeek.isoDayNumber
                    currentWeek.minus(kotlinx.datetime.DatePeriod(days = dayOfWeek - 1)) // Start of the week (Monday)
                }
                TrendGranularity.MONTHLY -> {
                    val currentMonth = now.date.minus(kotlinx.datetime.DatePeriod(months = i))
                    LocalDate(currentMonth.year, currentMonth.month, 1)
                }
            }
            trendsMap[periodDate] = 0f
        }

        // Aggregate activity values into the trendsMap
        allActivities.forEach { activity ->
            val activityDateTime = Instant.parse(activity.startDate).toLocalDateTime(TimeZone.currentSystemDefault())
            val periodStartDate = when (granularity) {
                TrendGranularity.WEEKLY -> {
                    val dayOfWeek = activityDateTime.dayOfWeek.isoDayNumber
                    activityDateTime.date.minus(kotlinx.datetime.DatePeriod(days = dayOfWeek - 1))
                }
                TrendGranularity.MONTHLY -> {
                    LocalDate(activityDateTime.year, activityDateTime.month, 1)
                }
            }

            // Only add to map if it's one of the periods we're tracking
            if (trendsMap.containsKey(periodStartDate)) {
                val value = when (metric) {
                    TrendMetric.DISTANCE -> activity.distanceKm
                    TrendMetric.DURATION -> activity.movingTimeSeconds.toFloat()
                    TrendMetric.ELEVATION -> activity.totalElevationGainMeters
                }
                trendsMap[periodStartDate] = trendsMap.getOrDefault(periodStartDate, 0f) + value
            }
        }

        return trendsMap.entries
            .map { TrendDataPoint(it.key, it.value) }
            .sortedBy { it.date }
    }
}
