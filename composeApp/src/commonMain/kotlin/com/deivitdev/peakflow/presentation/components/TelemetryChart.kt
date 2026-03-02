package com.deivitdev.peakflow.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun TelemetryChart(
    data: List<Float>,
    modifier: Modifier = Modifier
) {
    val accentColor = MaterialTheme.colorScheme.primary
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)

    Canvas(modifier = modifier.fillMaxWidth().height(150.dp)) {
        if (data.size < 2) return@Canvas

        val width = size.width
        val height = size.height
        val maxData = data.maxOrNull() ?: 1f
        val minData = data.minOrNull() ?: 0f
        val range = (maxData - minData).let { if (it == 0f) 1f else it }

        val path = Path()
        val fillPath = Path()

        data.forEachIndexed { index, value ->
            val x = index * (width / (data.size - 1))
            val y = height - ((value - minData) / range * height)

            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }

            if (index == data.size - 1) {
                fillPath.lineTo(x, height)
                fillPath.close()
            }
        }

        // Draw background fill
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(accentColor.copy(alpha = 0.3f), Color.Transparent),
                startY = 0f,
                endY = height
            )
        )

        // Draw the line
        drawPath(
            path = path,
            color = accentColor,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}
