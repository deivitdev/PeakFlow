package com.deivitdev.peakflow.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
actual fun ActualInteractiveMap(
    polyline: String,
    modifier: Modifier,
    isInteractive: Boolean,
    showMarkers: Boolean
) {
    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Text("Maps on iOS not yet implemented", style = MaterialTheme.typography.labelSmall)
    }
}
