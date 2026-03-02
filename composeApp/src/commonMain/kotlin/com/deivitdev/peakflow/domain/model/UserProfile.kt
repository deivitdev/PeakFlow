package com.deivitdev.peakflow.domain.model

data class UserProfile(
    val name: String,
    val weightKg: Float,
    val heightCm: Float,
    val isDarkMode: Boolean? = null, // null means use system default
    val language: String? = null, // 'en' or 'es', null means system default
    val profilePhotoUrl: String? = null,
    val ftpWatts: Int? = null,
    val hrMaxBpm: Int? = null,
    val weeklyGoalHours: Float = 5f
)
