package com.deivitdev.peakflow.domain.model

import androidx.compose.ui.graphics.Color

data class HeartRateZone(
    val id: Int, // 1 to 5
    val name: String,
    val durationSeconds: Int,
    val percentage: Float,
    val color: Color
)
