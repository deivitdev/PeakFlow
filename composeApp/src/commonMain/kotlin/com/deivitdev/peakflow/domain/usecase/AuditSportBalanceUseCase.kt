package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.model.BalanceAlert
import com.deivitdev.peakflow.domain.model.WorkoutType
import kotlinx.datetime.*

class AuditSportBalanceUseCase {
    operator fun invoke(activities: List<Activity>): BalanceAlert? {
        val now = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val last7DaysStart = now.minus(7, DateTimeUnit.DAY)

        val recentActivities = activities.filter { 
            Instant.parse(it.startDate).toLocalDateTime(TimeZone.currentSystemDefault()).date >= last7DaysStart
        }

        // 1. Check for Strength Neglect
        val hasStrength = recentActivities.any { it.type is WorkoutType.STRENGTH }
        if (!hasStrength && recentActivities.size >= 3) {
            return BalanceAlert.STRENGTH_NEGLECT
        }

        // 2. Check for Overtraining / Burnout risk (very high frequency)
        if (recentActivities.size >= 6) {
            return BalanceAlert.HIGH_FREQUENCY
        }

        return null
    }
}
