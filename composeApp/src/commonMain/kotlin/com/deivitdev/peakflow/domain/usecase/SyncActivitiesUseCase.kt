package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.repository.ActivityRepository

class SyncActivitiesUseCase(private val repository: ActivityRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.syncActivities()
    }
}
