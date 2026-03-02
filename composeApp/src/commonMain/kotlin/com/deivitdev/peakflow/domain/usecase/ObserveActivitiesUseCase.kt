package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.repository.ActivityRepository
import kotlinx.coroutines.flow.Flow

class ObserveActivitiesUseCase(private val repository: ActivityRepository) {
    operator fun invoke(): Flow<List<Activity>> {
        return repository.getActivitiesFlow()
    }
}
