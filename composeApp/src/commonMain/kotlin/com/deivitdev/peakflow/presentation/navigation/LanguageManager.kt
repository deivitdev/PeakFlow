package com.deivitdev.peakflow.presentation.navigation

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object LanguageManager {
    private val _currentLanguage = MutableStateFlow<String?>(null) // null means system default
    val currentLanguage: StateFlow<String?> = _currentLanguage.asStateFlow()

    fun setLanguage(language: String?) {
        _currentLanguage.value = language
    }
}

val LocalLanguage = staticCompositionLocalOf { "en" }
