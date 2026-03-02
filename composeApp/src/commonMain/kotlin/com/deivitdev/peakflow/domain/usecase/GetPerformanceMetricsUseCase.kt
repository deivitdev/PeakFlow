package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.ActivityTypeMetrics
import com.deivitdev.peakflow.domain.model.PerformanceMetrics
import com.deivitdev.peakflow.domain.model.WorkoutType
import com.deivitdev.peakflow.domain.repository.ActivityRepository
import com.deivitdev.peakflow.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.*

class GetPerformanceMetricsUseCase(
    private val repository: ActivityRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(dateRange: Int? = null): PerformanceMetrics {
        val allActivities = repository.getActivities()
        val userProfile = userRepository.getUserProfile().firstOrNull()
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())

        // Calculate Start Date and Goal Hours based on Calendar
        val (startDateLimit, goalHours) = if (dateRange == 30) {
            // Monthly: from 1st of current month
            val startOfMonth = LocalDate(now.year, now.month, 1)
            val daysInMonth = startOfMonth.plus(1, DateTimeUnit.MONTH).minus(1, DateTimeUnit.DAY).dayOfMonth
            val monthlyGoal = ((userProfile?.weeklyGoalHours ?: 5f) / 7f) * daysInMonth
            startOfMonth to monthlyGoal
        } else {
            // Weekly: from current Monday
            val dayOfWeek = now.dayOfWeek.isoDayNumber
            val startOfWeek = now.date.minus(DatePeriod(days = dayOfWeek - 1))
            startOfWeek to (userProfile?.weeklyGoalHours ?: 5f)
        }

        // Filter activities for this specific period
        val periodActivities = allActivities.filter { activity ->
            val activityDate = Instant.parse(activity.startDate)
                .toLocalDateTime(TimeZone.currentSystemDefault()).date
            activityDate >= startDateLimit
        }
        
        if (periodActivities.isEmpty()) {
            return PerformanceMetrics(0f, 0, 0f, 0f, 0, WorkoutType.OTHER, emptyMap(), 0f, "NO DATA")
        }

        val totalDistance = periodActivities.sumOf { it.distanceKm.toDouble() }.toFloat()
        val totalDuration = periodActivities.sumOf { it.movingTimeSeconds.toLong() }.toInt()
        val totalElevation = periodActivities.sumOf { it.totalElevationGainMeters.toDouble() }.toFloat()
        val averageSpeed = if (totalDuration > 0) (totalDistance / (totalDuration / 3600f)) else 0f
        
        val typeCounts = periodActivities.groupingBy { it.type }.eachCount()
        val dominantType = typeCounts.maxByOrNull { it.value }?.key ?: WorkoutType.OTHER

        // Calculate breakdown
        val breakdownMap = periodActivities.groupBy { activity ->
            when (activity.type) {
                is WorkoutType.CYCLING -> WorkoutType.CYCLING.GENERIC
                is WorkoutType.RUNNING -> WorkoutType.RUNNING.GENERIC
                else -> activity.type
            }
        }.mapValues { (type, typeActivities) ->
            val count = typeActivities.size
            val distance = typeActivities.sumOf { it.distanceKm.toDouble() }.toFloat()
            val duration = typeActivities.sumOf { it.movingTimeSeconds.toLong() }.toInt()
            ActivityTypeMetrics(type, count, distance, duration)
        }

        // Calculate Goal Progress
        val progressDurationSeconds = periodActivities.sumOf { it.movingTimeSeconds.toLong() }
        val goalSeconds = goalHours * 3600
        val progress = if (goalSeconds > 0) (progressDurationSeconds.toFloat() / goalSeconds).coerceAtMost(1.0f) else 0f
        
        val status = when {
            progress >= 1.0f -> "COMPLETED"
            progress >= 0.75f -> "AHEAD"
            progress >= 0.40f -> "ON TRACK"
            else -> "BEHIND"
        }

        return PerformanceMetrics(
            totalDistanceKm = totalDistance,
            totalDurationSeconds = totalDuration,
            totalElevationGainMeters = totalElevation,
            averageSpeedKmh = averageSpeed,
            activityCount = periodActivities.size,
            dominantType = dominantType,
            breakdown = breakdownMap,
            goalProgress = progress,
            goalStatus = status
        )
    }
}
