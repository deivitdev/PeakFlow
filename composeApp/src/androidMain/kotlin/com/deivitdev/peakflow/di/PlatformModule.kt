package com.deivitdev.peakflow.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.deivitdev.peakflow.AndroidPlatform
import com.deivitdev.peakflow.Platform
import com.deivitdev.peakflow.db.PeakFlowDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<SqlDriver> {
        AndroidSqliteDriver(PeakFlowDatabase.Schema, androidContext(), "peakflow.db")
    }
    single<Platform> { AndroidPlatform() }
}
