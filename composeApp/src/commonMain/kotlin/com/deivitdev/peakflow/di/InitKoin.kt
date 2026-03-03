package com.deivitdev.peakflow.di

import com.deivitdev.peakflow.domain.model.StravaConfig
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration

fun initKoin(
    config: StravaConfig,
    appDeclaration: KoinAppDeclaration = {}
) = startKoin {
    appDeclaration()
    modules(
        platformModule,
        dataModule,
        useCaseModule,
        viewModelModule,
        org.koin.dsl.module { single { config } }
    )
}
