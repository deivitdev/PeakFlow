package com.deivitdev.peakflow.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun RouteMap(
    polyline: String?,
    modifier: Modifier = Modifier,
    lineColor: Color = MaterialTheme.colorScheme.primary
) {
    if (polyline == null) return

    val points = decodePolyline(polyline)
    if (points.isEmpty()) return

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val minLat = points.minOf { it.first }
            val maxLat = points.maxOf { it.first }
            val minLng = points.minOf { it.second }
            val maxLng = points.maxOf { it.second }

            val latRange = maxLat - minLat
            val lngRange = maxLng - minLng

            // Maintain aspect ratio
            val scale = minOf(
                (width * 0.8f) / lngRange.toFloat(),
                (height * 0.8f) / latRange.toFloat()
            )

            val xOffset = (width - (lngRange.toFloat() * scale)) / 2f
            val yOffset = (height - (latRange.toFloat() * scale)) / 2f

            val path = Path()
            points.forEachIndexed { index, point ->
                val x = xOffset + (point.second - minLng).toFloat() * scale
                val y = height - (yOffset + (point.first - minLat).toFloat() * scale)

                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }
            }

            drawPath(
                path = path,
                color = lineColor,
                style = Stroke(width = 3.dp.toPx())
            )
        }
    }
}
