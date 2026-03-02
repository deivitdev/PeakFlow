package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.repository.ActivityRepository
import com.deivitdev.peakflow.domain.repository.UserRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class SyncDailyLoadUseCase(
    private val activityRepository: ActivityRepository,
    private val userRepository: UserRepository,
    private val calculateMetrics: CalculatePerformanceMetricsUseCase
) {
    suspend operator fun invoke() {
        val activities = activityRepository.getActivities()
        val profile = userRepository.getUserProfile().firstOrNull()
        
        // Group activities by date (YYYY-MM-DD)
        val dailyTssMap = activities.groupBy { activity ->
            Instant.parse(activity.startDate)
                .toLocalDateTime(TimeZone.currentSystemDefault())
                .date.toString()
        }.mapValues { (_, activitiesOnDate) ->
            activitiesOnDate.sumOf { activity ->
                calculateMetrics(activity, profile).tssValue?.toDouble() ?: 0.0
            }.toFloat()
        }

        activityRepository.clearDailyLoads()
        dailyTssMap.forEach { (date, tss) ->
            if (tss > 0) {
                activityRepository.saveDailyLoad(date, tss)
            }
        }
    }
}
