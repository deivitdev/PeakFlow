package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.FitnessFatigueData
import com.deivitdev.peakflow.domain.model.TrainingRecommendation
import com.deivitdev.peakflow.domain.repository.ActivityRepository

class GetPredictiveCoachingUseCase(
    private val activityRepository: ActivityRepository,
    private val predictLoad: PredictTrainingLoadUseCase,
    private val auditBalance: AuditSportBalanceUseCase
) {
    suspend operator fun invoke(ffData: FitnessFatigueData): TrainingRecommendation {
        val activities = activityRepository.getActivities()
        val options = predictLoad(ffData)
        val alert = auditBalance(activities)

        return TrainingRecommendation(
            options = options,
            priorityAlert = alert
        )
    }
}
