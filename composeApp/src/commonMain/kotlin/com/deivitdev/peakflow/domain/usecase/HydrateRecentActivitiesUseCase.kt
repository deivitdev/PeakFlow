package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.repository.ActivityRepository

class HydrateRecentActivitiesUseCase(private val repository: ActivityRepository) {
    suspend operator fun invoke(count: Int = 5) {
        val activities = repository.getActivities()
        activities.take(count).forEach { summary ->
            // This will fetch full details and streams if missing and save to DB
            repository.getActivity(summary.id)
        }
    }
}
