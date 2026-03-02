package com.deivitdev.peakflow.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TelemetrySeries(
    val data: List<Float>,
    val color: Color,
    val displayLabel: String,
    val alignmentKey: String // "HR", "ELEV", "CAD"
)

@Composable
fun InteractiveMultiTelemetryChart(
    seriesList: List<TelemetrySeries>,
    hoveredIndex: Int?,
    onHoverChanged: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    overlayContent: @Composable (BoxScope.(Int) -> Unit) = {}
) {
    if (seriesList.isEmpty()) return

    val dataSize = seriesList.first().data.size
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {
        // Y-Axis Labels
        // Filter series for unique alignments to avoid overlapping labels too much
        val leftSeries = seriesList.filter { it.alignmentKey == "HR" || it.alignmentKey == "CAD" }.take(1)
        val rightSeries = seriesList.filter { it.alignmentKey == "ELEV" }.take(1)

        leftSeries.forEach { series ->
            AxisLabels(series, Alignment.CenterStart, labelColor)
        }
        
        rightSeries.forEach { series ->
            AxisLabels(series, Alignment.CenterEnd, labelColor)
        }

        // Chart Area
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 8.dp)
                .pointerInput(seriesList) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            val index = (offset.x / size.width * dataSize).toInt().coerceIn(0, dataSize - 1)
                            onHoverChanged(index)
                        },
                        onDrag = { change, _ ->
                            val index = (change.position.x / size.width * dataSize).toInt().coerceIn(0, dataSize - 1)
                            onHoverChanged(index)
                        },
                        onDragEnd = { onHoverChanged(null) },
                        onDragCancel = { onHoverChanged(null) }
                    )
                }
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                if (dataSize < 2) return@Canvas

                val width = size.width
                val height = size.height

                seriesList.forEach { series ->
                    val data = series.data
                    val accentColor = series.color
                    val maxData = data.maxOrNull()?.let { if (it == 0f) 1f else it } ?: 1f
                    val minData = data.minOrNull() ?: 0f
                    val range = (maxData - minData).let { if (it == 0f) 1f else it }

                    val path = Path()
                    val fillPath = Path()

                    data.forEachIndexed { index, value ->
                        val x = index * (width / (dataSize - 1))
                        val y = height - ((value - minData) / range * height)

                        if (index == 0) {
                            path.moveTo(x, y)
                            fillPath.moveTo(x, height)
                            fillPath.lineTo(x, y)
                        } else {
                            path.lineTo(x, y)
                            fillPath.lineTo(x, y)
                        }

                        if (index == dataSize - 1) {
                            fillPath.lineTo(x, height)
                            fillPath.close()
                        }
                    }

                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(accentColor.copy(alpha = 0.1f), Color.Transparent),
                            startY = 0f,
                            endY = height
                        )
                    )

                    drawPath(
                        path = path,
                        color = accentColor,
                        style = Stroke(width = 2.dp.toPx())
                    )

                    hoveredIndex?.let { index ->
                        val x = index * (width / (dataSize - 1))
                        val y = height - ((data[index] - minData) / range * height)

                        drawCircle(
                            color = accentColor.copy(alpha = 0.2f),
                            radius = 10.dp.toPx(),
                            center = Offset(x, y)
                        )
                        drawCircle(
                            color = Color.White,
                            radius = 4.dp.toPx(),
                            center = Offset(x, y)
                        )
                        drawCircle(
                            color = accentColor,
                            radius = 3.dp.toPx(),
                            center = Offset(x, y)
                        )
                    }
                }

                hoveredIndex?.let { index ->
                    val x = index * (width / (dataSize - 1))
                    drawLine(
                        color = Color.White.copy(alpha = 0.2f),
                        start = Offset(x, 0f),
                        end = Offset(x, height),
                        strokeWidth = 1.dp.toPx()
                    )
                }
            }

            hoveredIndex?.let { index ->
                overlayContent(index)
            }
        }
    }
}

@Composable
private fun BoxScope.AxisLabels(series: TelemetrySeries, alignment: Alignment, labelColor: Color) {
    val isLeft = alignment == Alignment.CenterStart
    val maxVal = series.data.maxOrNull()?.toInt() ?: 0
    val minVal = series.data.minOrNull()?.toInt() ?: 0

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .align(alignment)
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = if (isLeft) Alignment.Start else Alignment.End
    ) {
        Text(
            text = "$maxVal",
            style = MaterialTheme.typography.labelSmall,
            color = series.color.copy(alpha = 0.8f),
            fontFamily = FontFamily.Monospace,
            fontSize = 9.sp
        )
        Text(
            text = series.displayLabel,
            style = MaterialTheme.typography.labelSmall,
            color = labelColor,
            fontWeight = FontWeight.Bold,
            fontSize = 8.sp
        )
        Text(
            text = "$minVal",
            style = MaterialTheme.typography.labelSmall,
            color = series.color.copy(alpha = 0.8f),
            fontFamily = FontFamily.Monospace,
            fontSize = 9.sp
        )
    }
}
