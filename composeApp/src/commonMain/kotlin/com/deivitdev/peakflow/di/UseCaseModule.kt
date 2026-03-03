package com.deivitdev.peakflow.di

import com.deivitdev.peakflow.domain.usecase.*
import org.koin.dsl.module

val useCaseModule = module {
    factory { GetUserProfileUseCase(get()) }
    factory { SaveUserProfileUseCase(get()) }
    factory { GetPerformanceMetricsUseCase(get(), get()) }
    factory { GetActivityDetailUseCase(get()) }
    factory { CalculatePerformanceMetricsUseCase() }
    factory { CalculateRelativePowerUseCase() }
    factory { GetTrendsUseCase(get()) }
    factory { SyncDailyLoadUseCase(get(), get(), get()) }
    factory { GetFitnessFatigueUseCase(get()) }
    factory { CalculateMetabolicOxidationUseCase() }
    factory { CalculatePolarizationRatioUseCase() }
    factory { GetAdvancedPerformanceInsightsUseCase(get(), get(), get(), get()) }
    factory { PredictTrainingLoadUseCase() }
    factory { AuditSportBalanceUseCase() }
    factory { GetPredictiveCoachingUseCase(get(), get(), get()) }
    factory { SyncActivitiesUseCase(get()) }
    factory { ObserveActivitiesUseCase(get()) }
    factory { HydrateRecentActivitiesUseCase(get()) }
    factory { GetLanguageUseCase(get()) }
}
