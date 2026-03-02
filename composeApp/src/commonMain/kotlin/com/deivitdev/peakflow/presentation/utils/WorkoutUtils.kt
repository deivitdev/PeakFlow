package com.deivitdev.peakflow.presentation.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun getIntensityColor(sufferScore: Int?): Color {
    return when {
        sufferScore == null -> MaterialTheme.colorScheme.primary
        sufferScore < 15 -> Color(0xFF4CAF50)
        sufferScore < 30 -> Color(0xFFFFEB3B)
        sufferScore < 60 -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }
}
