package com.deivitdev.peakflow.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.swmansion.kmpmaps.core.*
import kotlin.math.*

@Composable
actual fun ActualInteractiveMap(
    polyline: String,
    modifier: Modifier,
    isInteractive: Boolean,
    showMarkers: Boolean
) {
    val points = decodePolyline(polyline)
    if (points.isEmpty()) return

    val routeCoordinates = points.map { Coordinates(it.first, it.second) }
    
    // 1. Calculate Bounds & Camera
    val minLat = points.minOf { it.first }
    val maxLat = points.maxOf { it.first }
    val minLng = points.minOf { it.second }
    val maxLng = points.maxOf { it.second }
    val centerLat = (minLat + maxLat) / 2.0
    val centerLng = (minLng + maxLng) / 2.0
    val maxDiff = max(maxLat - minLat, maxLng - minLng)
    val zoom = when {
        maxDiff < 0.005 -> 16f
        maxDiff < 0.01 -> 15f
        maxDiff < 0.02 -> 14f
        maxDiff < 0.05 -> 13f
        maxDiff < 0.1 -> 12f
        else -> 11f
    }

    val isDark = isSystemInDarkTheme()

    // 2. Markers & Custom Content Map
    val markers = mutableListOf<Marker>()
    val customContent = mutableMapOf<String, @Composable () -> Unit>()
    
    if (showMarkers) {
        // Start Marker
        markers.add(Marker(coordinates = routeCoordinates.first(), contentId = "start-node"))
        customContent["start-node"] = {
            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(Color(0xFF4CAF50)))
        }

        // End Marker
        markers.add(Marker(coordinates = routeCoordinates.last(), contentId = "end-node"))
        customContent["end-node"] = {
            Icon(Icons.Default.Flag, null, tint = if (isDark) Color.White else Color.Black, modifier = Modifier.size(24.dp))
        }
    }

    Map(
        modifier = modifier.fillMaxSize(),
        properties = MapProperties(
            isMyLocationEnabled = false,
            mapTheme = if (isDark) MapTheme.DARK else MapTheme.LIGHT
        ),
        uiSettings = MapUISettings(
            scrollEnabled = isInteractive,
            zoomEnabled = isInteractive,
            rotateEnabled = isInteractive,
            togglePitchEnabled = isInteractive,
            myLocationButtonEnabled = false,
            compassEnabled = isInteractive,
            androidUISettings = AndroidUISettings(zoomControlsEnabled = false)
        ),
        cameraPosition = CameraPosition(
            coordinates = Coordinates(centerLat, centerLng),
            zoom = zoom
        ),
        polylines = listOf(
            Polyline(
                coordinates = routeCoordinates,
                lineColor = Color(0xFFFF3D00),
                width = 12f
            )
        ),
        markers = markers,
        customMarkerContent = customContent
    )
}
