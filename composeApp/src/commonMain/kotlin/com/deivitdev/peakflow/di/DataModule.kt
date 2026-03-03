package com.deivitdev.peakflow.di

import com.deivitdev.peakflow.data.local.LocalDataSource
import com.deivitdev.peakflow.data.remote.StravaApiClient
import com.deivitdev.peakflow.data.repository.ActivityRepositoryImpl
import com.deivitdev.peakflow.data.repository.UserRepositoryImpl
import com.deivitdev.peakflow.db.PeakFlowDatabase
import com.deivitdev.peakflow.domain.repository.ActivityRepository
import com.deivitdev.peakflow.domain.repository.UserRepository
import org.koin.dsl.module

val dataModule = module {
    single { StravaApiClient(StravaApiClient.createDefaultHttpClient()) }
    single { PeakFlowDatabase(get()) }
    single { LocalDataSource(get()) }
    
    single<ActivityRepository> { ActivityRepositoryImpl(get(), get(), get()) }
    single<UserRepository> { UserRepositoryImpl(get()) }
}
