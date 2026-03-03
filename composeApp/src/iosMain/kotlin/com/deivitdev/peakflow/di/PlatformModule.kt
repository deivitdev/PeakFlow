package com.deivitdev.peakflow.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.deivitdev.peakflow.IOSPlatform
import com.deivitdev.peakflow.Platform
import com.deivitdev.peakflow.db.PeakFlowDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> {
        NativeSqliteDriver(PeakFlowDatabase.Schema, "peakflow.db")
    }
    single<Platform> { IOSPlatform() }
}
