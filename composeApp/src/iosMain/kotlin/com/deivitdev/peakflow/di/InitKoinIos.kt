package com.deivitdev.peakflow.di

import com.deivitdev.peakflow.domain.model.StravaConfig
import org.koin.core.context.startKoin

fun initKoinIos(config: StravaConfig) = initKoin(config) {}
