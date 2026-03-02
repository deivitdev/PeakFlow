package com.deivitdev.peakflow

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
    
    override fun setLanguage(language: String?) {
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        val newLocaleList = if (language == null) {
            LocaleListCompat.getEmptyLocaleList()
        } else {
            LocaleListCompat.forLanguageTags(language)
        }

        // Solo aplicamos si el idioma es distinto al actual
        if (currentLocales.toLanguageTags() != newLocaleList.toLanguageTags()) {
            AppCompatDelegate.setApplicationLocales(newLocaleList)
        }
    }
}

actual fun getPlatform(): Platform = AndroidPlatform()
