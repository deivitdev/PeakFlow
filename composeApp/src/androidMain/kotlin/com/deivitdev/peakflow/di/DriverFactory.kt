package com.deivitdev.peakflow.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.deivitdev.peakflow.db.PeakFlowDatabase

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(PeakFlowDatabase.Schema, context, "peakflow.db")
    }
}
