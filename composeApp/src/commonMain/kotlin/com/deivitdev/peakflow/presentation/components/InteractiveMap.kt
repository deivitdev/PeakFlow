package com.deivitdev.peakflow.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun InteractiveMap(
    polyline: String,
    modifier: Modifier = Modifier,
    isInteractive: Boolean = true,
    showMarkers: Boolean = true
) {
    ActualInteractiveMap(polyline, modifier, isInteractive, showMarkers)
}

@Composable
expect fun ActualInteractiveMap(
    polyline: String,
    modifier: Modifier,
    isInteractive: Boolean,
    showMarkers: Boolean
)
