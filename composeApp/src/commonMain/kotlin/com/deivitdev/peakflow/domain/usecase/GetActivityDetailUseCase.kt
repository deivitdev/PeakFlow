package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.repository.ActivityRepository

class GetActivityDetailUseCase(private val repository: ActivityRepository) {
    suspend operator fun invoke(id: String): Activity? {
        return repository.getActivity(id)
    }
}
