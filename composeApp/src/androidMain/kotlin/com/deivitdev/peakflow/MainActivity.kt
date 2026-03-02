package com.deivitdev.peakflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import com.deivitdev.peakflow.di.DriverFactory
import com.deivitdev.peakflow.db.PeakFlowDatabase
import com.deivitdev.peakflow.db.UserProfile
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private val stravaAuthCode = mutableStateOf<String?>(null)
    
    private lateinit var database: PeakFlowDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        val driver = DriverFactory(applicationContext).createDriver()
        database = PeakFlowDatabase(driver)

        // Load profile synchronously to prevent UI flickering on recreation
        val initialProfile = runBlocking {
            try {
                database.peakFlowDatabaseQueries.getUserProfile().executeAsOneOrNull()
            } catch (e: Exception) {
                null
            }
        }

        // Apply language before content is set
        initialProfile?.let {
            getPlatform().setLanguage(it.language)
        }

        handleIntent(intent)

        setContent {
            App(
                database = database,
                initialProfile = initialProfile,
                stravaAuthCode = stravaAuthCode.value,
                onAuthCodeHandled = { stravaAuthCode.value = null },
                stravaClientId = BuildConfig.STRAVA_CLIENT_ID,
                stravaClientSecret = BuildConfig.STRAVA_CLIENT_SECRET
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_VIEW) {
            val data = intent.data
            if (data?.scheme == "peakflow" && data.host == "strava-auth") {
                val code = data.getQueryParameter("code")
                if (code != null) {
                    stravaAuthCode.value = code
                }
            }
        }
    }
}
