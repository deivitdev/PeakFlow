package com.deivitdev.peakflow.presentation.analytics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deivitdev.peakflow.domain.usecase.TrendDataPoint
import com.deivitdev.peakflow.domain.usecase.TrendGranularity
import com.deivitdev.peakflow.domain.usecase.TrendMetric
import com.deivitdev.peakflow.presentation.components.SectionTitle
import com.deivitdev.peakflow.presentation.utils.formatDecimal
import com.deivitdev.peakflow.presentation.utils.formatSeconds
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*
import kotlin.math.roundToInt

@OptIn(ExperimentalTextApi::class)
@Composable
fun TrendsChartCard(
    dataPoints: List<TrendDataPoint>,
    metric: TrendMetric,
    granularity: TrendGranularity,
    modifier: Modifier = Modifier,
    chartColor: Color = MaterialTheme.colorScheme.primary
) {
    var selectedPoint by remember(dataPoints) { mutableStateOf<TrendDataPoint?>(null) }
    val isSpanish = stringResource(Res.string.km) == "km" && stringResource(Res.string.total_distance) == "Distancia Total"

    Column(modifier = modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        ) {
            Column(Modifier.padding(16.dp)) {
                // 1. Interactive HUD (Heads-Up Display)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(bottom = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (selectedPoint != null) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            val monthNames = if (isSpanish) getSpanishMonths() else getEnglishMonths()
                            val dateLabel = when (granularity) {
                                TrendGranularity.WEEKLY -> "${monthNames[selectedPoint!!.date.month]} ${selectedPoint!!.date.dayOfMonth}"
                                TrendGranularity.MONTHLY -> "${monthNames[selectedPoint!!.date.month]} '${selectedPoint!!.date.year.toString().takeLast(2)}"
                            }
                            
                            Column {
                                Text(
                                    text = dateLabel.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = when (metric) {
                                        TrendMetric.DISTANCE -> "${formatDecimal(selectedPoint!!.value)} ${stringResource(Res.string.km)}"
                                        TrendMetric.DURATION -> formatSeconds(selectedPoint!!.value.toInt())
                                        TrendMetric.ELEVATION -> "${selectedPoint!!.value.toInt()} ${stringResource(Res.string.meters)}"
                                    },
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    } else {
                        Text(
                            text = if (isSpanish) "DESLIZA PARA VER DETALLES" else "SCROLL TO SEE DETAILS",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                if (dataPoints.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(stringResource(Res.string.no_trends_data))
                    }
                } else {
                    LineChart(
                        dataPoints = dataPoints,
                        chartColor = chartColor,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(horizontal = 8.dp, vertical = 12.dp),
                        metric = metric,
                        granularity = granularity,
                        onPointSelected = { selectedPoint = it }
                    )
                }
            }
        }
    }
}

private fun getSpanishMonths() = mapOf(
    Month.JANUARY to "Ene", Month.FEBRUARY to "Feb", Month.MARCH to "Mar",
    Month.APRIL to "Abr", Month.MAY to "May", Month.JUNE to "Jun",
    Month.JULY to "Jul", Month.AUGUST to "Ago", Month.SEPTEMBER to "Sep",
    Month.OCTOBER to "Oct", Month.NOVEMBER to "Nov", Month.DECEMBER to "Dic"
)

private fun getEnglishMonths() = Month.entries.associateWith { it.name.take(3) }

@OptIn(ExperimentalTextApi::class)
@Composable
private fun LineChart(
    dataPoints: List<TrendDataPoint>,
    chartColor: Color,
    metric: TrendMetric,
    granularity: TrendGranularity,
    onPointSelected: (TrendDataPoint?) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val labelTextStyle = TextStyle(color = Color.Gray, fontSize = 10.sp)
    val isSpanish = stringResource(Res.string.km) == "km" && stringResource(Res.string.total_distance) == "Distancia Total"
    
    var touchX by remember { mutableStateOf<Float?>(null) }

    Canvas(
        modifier = modifier
            .pointerInput(dataPoints) {
                detectTapGestures(
                    onPress = { offset ->
                        touchX = offset.x
                        tryAwaitRelease()
                        touchX = null
                        onPointSelected(null)
                    }
                )
            }
            .pointerInput(dataPoints) {
                detectDragGestures(
                    onDragStart = { offset -> touchX = offset.x },
                    onDrag = { change, _ -> 
                        touchX = change.position.x 
                        change.consume()
                    },
                    onDragEnd = { 
                        touchX = null 
                        onPointSelected(null)
                    },
                    onDragCancel = { 
                        touchX = null 
                        onPointSelected(null)
                    }
                )
            }
    ) {
        val width = size.width
        val height = size.height

        val maxValue = dataPoints.maxOfOrNull { it.value } ?: 0f
        val minValue = 0f 
        val range = if (maxValue > minValue) maxValue - minValue else 1f

        val stepX = width / (dataPoints.size - 1).toFloat()

        // 1. Draw Horizontal Grid Lines & Y-Axis Labels
        val gridLines = 4
        for (i in 0..gridLines) {
            val y = height - (i.toFloat() / gridLines) * height
            drawLine(
                color = Color.Gray.copy(alpha = 0.15f),
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.dp.toPx()
            )
            
            val labelValue = minValue + (i.toFloat() / gridLines) * range
            val labelText = when (metric) {
                TrendMetric.DURATION -> "${(labelValue / 3600).toInt()}h"
                else -> labelValue.toInt().toString()
            }
            
            val textLayoutResult = textMeasurer.measure(labelText, style = labelTextStyle)
            drawText(
                textMeasurer = textMeasurer,
                text = labelText,
                style = labelTextStyle,
                topLeft = Offset(-textLayoutResult.size.width - 8.dp.toPx(), y - textLayoutResult.size.height / 2)
            )
        }

        // 2. Prepare Path and Gradient Fill
        val path = Path()
        val fillPath = Path()
        
        dataPoints.forEachIndexed { index, point ->
            val x = index * stepX
            val y = height - ((point.value - minValue) / range) * height

            if (index == 0) {
                path.moveTo(x, y)
                fillPath.moveTo(x, height)
                fillPath.lineTo(x, y)
            } else {
                path.lineTo(x, y)
                fillPath.lineTo(x, y)
            }
            
            if (index == dataPoints.size - 1) {
                fillPath.lineTo(x, height)
                fillPath.close()
            }

            // 3. Draw X-Axis Labels (dates)
            if (index % (dataPoints.size / 4).coerceAtLeast(1) == 0 || index == dataPoints.size - 1) {
                val monthNames = if (isSpanish) getSpanishMonths() else getEnglishMonths()
                val monthName = monthNames[point.date.month] ?: ""
                val dateLabel = when (granularity) {
                    TrendGranularity.WEEKLY -> "$monthName ${point.date.dayOfMonth}"
                    TrendGranularity.MONTHLY -> "$monthName '${point.date.year.toString().takeLast(2)}"
                }
                val textLayoutResult = textMeasurer.measure(dateLabel, style = labelTextStyle)
                drawText(
                    textMeasurer = textMeasurer,
                    text = dateLabel,
                    style = labelTextStyle,
                    topLeft = Offset(x - (textLayoutResult.size.width / 2), height + 8.dp.toPx())
                )
            }
        }

        // 4. Draw Gradient Fill
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(chartColor.copy(alpha = 0.3f), Color.Transparent),
                startY = 0f,
                endY = height
            )
        )

        // 5. Draw Main Curve
        drawPath(
            path = path,
            color = chartColor,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // 6. Draw Interaction Overlay (Scrubbing)
        touchX?.let { x ->
            val index = (x / stepX).roundToInt().coerceIn(0, dataPoints.size - 1)
            val point = dataPoints[index]
            val actualX = index * stepX
            val actualY = height - ((point.value - minValue) / range) * height
            
            onPointSelected(point)

            // Vertical guide line
            drawLine(
                color = chartColor.copy(alpha = 0.4f),
                start = Offset(actualX, 0f),
                end = Offset(actualX, height),
                strokeWidth = 1.5.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            )

            // Highlighted selection point
            drawCircle(
                color = Color.White,
                radius = 6.dp.toPx(),
                center = Offset(actualX, actualY)
            )
            drawCircle(
                color = chartColor,
                radius = 6.dp.toPx(),
                center = Offset(actualX, actualY),
                style = Stroke(width = 3.dp.toPx())
            )
        }
    }
}

@Preview
@Composable
fun PreviewTrendsChartCard() {
    val sampleData = listOf(
        TrendDataPoint(LocalDate(2023, kotlinx.datetime.Month.JANUARY, 1), 100f),
        TrendDataPoint(LocalDate(2023, kotlinx.datetime.Month.JANUARY, 8), 120f),
        TrendDataPoint(LocalDate(2023, kotlinx.datetime.Month.JANUARY, 15), 90f),
        TrendDataPoint(LocalDate(2023, kotlinx.datetime.Month.JANUARY, 22), 150f),
        TrendDataPoint(LocalDate(2023, kotlinx.datetime.Month.JANUARY, 29), 110f),
    )
    MaterialTheme {
        Surface {
            TrendsChartCard(
                dataPoints = sampleData,
                metric = TrendMetric.DISTANCE,
                granularity = TrendGranularity.WEEKLY
            )
        }
    }
}
