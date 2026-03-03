package com.deivitdev.peakflow

import android.app.Application
import com.deivitdev.peakflow.di.initKoin
import com.deivitdev.peakflow.domain.model.StravaConfig
import org.koin.android.ext.koin.androidContext

class PeakFlowApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        initKoin(
            config = StravaConfig(
                clientId = BuildConfig.STRAVA_CLIENT_ID,
                clientSecret = BuildConfig.STRAVA_CLIENT_SECRET,
                redirectUri = "peakflow://strava-auth"
            )
        ) {
            androidContext(this@PeakFlowApp)
        }
    }
}
