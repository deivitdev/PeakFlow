package com.deivitdev.peakflow.di

import com.deivitdev.peakflow.presentation.analytics.AnalyticsViewModel
import com.deivitdev.peakflow.presentation.profile.ProfileViewModel
import com.deivitdev.peakflow.presentation.workout_list.WorkoutListViewModel
import com.deivitdev.peakflow.presentation.activity_detail.ActivityDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::AnalyticsViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::WorkoutListViewModel)
    viewModelOf(::ActivityDetailViewModel)
}
