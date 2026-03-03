package com.deivitdev.peakflow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import android.content.Intent
import com.deivitdev.peakflow.db.PeakFlowDatabase
import org.koin.android.ext.android.get
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private val stravaAuthCode = mutableStateOf<String?>(null)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        val database: PeakFlowDatabase = get()

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
                stravaAuthCode = stravaAuthCode.value,
                onAuthCodeHandled = { stravaAuthCode.value = null }
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
