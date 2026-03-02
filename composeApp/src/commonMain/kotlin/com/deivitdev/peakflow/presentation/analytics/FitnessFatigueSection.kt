package com.deivitdev.peakflow.presentation.analytics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deivitdev.peakflow.domain.model.FitnessFatigueData
import com.deivitdev.peakflow.domain.model.FitnessFatiguePoint
import com.deivitdev.peakflow.domain.model.TrainingStatus
import com.deivitdev.peakflow.presentation.components.SectionTitle
import com.deivitdev.peakflow.presentation.components.MetricInfoTooltip
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*
import kotlin.math.roundToInt

@Composable
fun FitnessFatigueSection(
    data: FitnessFatigueData,
    onShowTsbGuide: () -> Unit
) {
    var selectedPoint by remember(data) { mutableStateOf<FitnessFatiguePoint?>(null) }
    val isSpanish = stringResource(Res.string.km) == "km" && stringResource(Res.string.total_distance) == "Distancia Total"
    
    // Theme-aware colors
    val isDark = isSystemInDarkTheme()
    val fitnessColor = Color(0xFF2196F3)
    val fatigueColor = Color(0xFFF44336)
    val formColor = if (isDark) Color(0xFFFFEB3B) else Color(0xFFFFA000)

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionTitle(stringResource(Res.string.fitness_fatigue_title))
        
        TrainingStatusCard(data, formColor, onShowTsbGuide)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        ) {
            Column(Modifier.padding(16.dp)) {
                // 1. Interactive HUD
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(bottom = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (selectedPoint != null) {
                        val date = LocalDate.parse(selectedPoint!!.date)
                        val monthNames = if (isSpanish) getSpanishMonths() else getEnglishMonths()
                        val dateLabel = "${monthNames[date.month]} ${date.dayOfMonth}"
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = dateLabel.uppercase(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "POINT DATA",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                    fontSize = 8.sp
                                )
                            }
                            
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                QuickMetric("CTL", selectedPoint!!.fitness.roundToInt(), fitnessColor)
                                QuickMetric("ATL", selectedPoint!!.fatigue.roundToInt(), fatigueColor)
                                QuickMetric("TSB", selectedPoint!!.form.roundToInt(), formColor)
                            }
                        }
                    } else {
                        Text(
                            text = if (isSpanish) "DESLIZA LA GRÁFICA PARA VER VALORES" else "SCRUB CHART TO SEE VALUES",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }

                FitnessFatigueChart(
                    data = data,
                    fitnessColor = fitnessColor,
                    fatigueColor = fatigueColor,
                    formColor = formColor,
                    onPointSelected = { selectedPoint = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .padding(horizontal = 4.dp)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LegendItem(stringResource(Res.string.fitness_label), fitnessColor)
                        Spacer(modifier = Modifier.width(4.dp))
                        MetricInfoTooltip(acronym = "CTL")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LegendItem(stringResource(Res.string.fatigue_label), fatigueColor)
                        Spacer(modifier = Modifier.width(4.dp))
                        MetricInfoTooltip(acronym = "ATL")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        LegendItem(stringResource(Res.string.form_label), formColor)
                        Spacer(modifier = Modifier.width(4.dp))
                        MetricInfoTooltip(acronym = "TSB")
                    }
                }
            }
        }
    }
}

@Composable
private fun QuickMetric(label: String, value: Int, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 8.sp, fontWeight = FontWeight.Bold, color = color.copy(alpha = 0.8f))
            Text(value.toString(), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Black, color = color)
        }
        Spacer(modifier = Modifier.width(4.dp))
    }
}

@Composable
private fun TrainingStatusCard(
    data: FitnessFatigueData, 
    formColor: Color,
    onShowTsbGuide: () -> Unit
) {
    val isDark = isSystemInDarkTheme()
    val (statusLabel, color) = when (data.currentStatus) {
        TrainingStatus.FRESH -> stringResource(Res.string.status_fresh) to Color(0xFF4CAF50)
        TrainingStatus.OPTIMAL -> stringResource(Res.string.status_optimal) to Color(0xFF2196F3)
        TrainingStatus.NEUTRAL -> stringResource(Res.string.status_neutral) to Color(0xFF9E9E9E)
        TrainingStatus.OVERREACHING -> stringResource(Res.string.status_overreaching) to Color(0xFFF44336)
        TrainingStatus.RECOVERY -> stringResource(Res.string.status_recovery) to (if (isDark) Color(0xFFFFEB3B) else Color(0xFFFFA000))
        TrainingStatus.BUILDING_DATA -> stringResource(Res.string.status_building) to Color(0xFF607D8B)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.training_status_label).uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    MetricInfoTooltip(acronym = "STATUS")
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = statusLabel,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = color
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "TSB",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = color.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    MetricInfoTooltip(acronym = "TSB")
                }
                Text(
                    text = data.currentForm.roundToInt().toString(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black,
                    color = color
                )
                Text(
                    text = stringResource(Res.string.tsb_guide_learn_more),
                    style = MaterialTheme.typography.labelSmall,
                    color = color.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold,
                    textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline,
                    modifier = Modifier.clickable { onShowTsbGuide() }
                )
            }
        }
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun FitnessFatigueChart(
    data: FitnessFatigueData,
    fitnessColor: Color,
    fatigueColor: Color,
    formColor: Color,
    onPointSelected: (FitnessFatiguePoint?) -> Unit,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()
    val labelColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
    val labelTextStyle = TextStyle(
        color = labelColor, 
        fontSize = 11.sp, 
        fontWeight = FontWeight.Medium
    )
    val isSpanish = stringResource(Res.string.km) == "km" && stringResource(Res.string.total_distance) == "Distancia Total"

    var touchX by remember { mutableStateOf<Float?>(null) }

    Canvas(
        modifier = modifier
            .pointerInput(data) {
                detectTapGestures(
                    onPress = { offset ->
                        touchX = offset.x
                        tryAwaitRelease()
                        touchX = null
                        onPointSelected(null)
                    }
                )
            }
            .pointerInput(data) {
                detectDragGestures(
                    onDragStart = { offset -> touchX = offset.x },
                    onDrag = { change, _ -> 
                        touchX = change.position.x 
                        change.consume()
                    },
                    onDragEnd = { touchX = null; onPointSelected(null) },
                    onDragCancel = { touchX = null; onPointSelected(null) }
                )
            }
    ) {
        val leftPadding = 36.dp.toPx()
        val bottomPadding = 24.dp.toPx()
        val topPadding = 8.dp.toPx()
        val rightPadding = 16.dp.toPx()
        
        val chartWidth = size.width - leftPadding - rightPadding
        val chartHeight = size.height - bottomPadding - topPadding
        
        val points = data.history
        if (points.isEmpty()) return@Canvas

        val maxVal = points.maxOf { maxOf(it.fitness, it.fatigue) }.coerceAtLeast(50f) * 1.2f
        val minVal = points.minOf { it.form }.coerceAtMost(-30f) * 1.2f
        val range = maxVal - minVal

        val stepX = chartWidth / (points.size - 1).toFloat()

        for (i in 0..4) {
            val ratio = i.toFloat() / 4
            val y = topPadding + chartHeight - (ratio * chartHeight)
            
            drawLine(
                color = labelColor.copy(alpha = 0.1f),
                start = Offset(leftPadding, y),
                end = Offset(leftPadding + chartWidth, y),
                strokeWidth = 1.dp.toPx()
            )
            
            val labelValue = minVal + (ratio * range)
            val textLayoutResult = textMeasurer.measure(labelValue.roundToInt().toString(), style = labelTextStyle)
            drawText(
                textMeasurer = textMeasurer,
                text = labelValue.roundToInt().toString(),
                style = labelTextStyle,
                topLeft = Offset(leftPadding - textLayoutResult.size.width - 8.dp.toPx(), y - textLayoutResult.size.height / 2)
            )
        }

        val zeroY = topPadding + chartHeight - ((0f - minVal) / range) * chartHeight
        drawLine(
            color = labelColor.copy(alpha = 0.3f),
            start = Offset(leftPadding, zeroY),
            end = Offset(leftPadding + chartWidth, zeroY),
            strokeWidth = 1.2.dp.toPx()
        )

        val fitnessPath = Path()
        val fatiguePath = Path()
        val formPath = Path()

        points.forEachIndexed { index, point ->
            val x = leftPadding + (index * stepX)
            val fitnessY = topPadding + chartHeight - ((point.fitness - minVal) / range) * chartHeight
            val fatigueY = topPadding + chartHeight - ((point.fatigue - minVal) / range) * chartHeight
            val formY = topPadding + chartHeight - ((point.form - minVal) / range) * chartHeight

            if (index == 0) {
                fitnessPath.moveTo(x, fitnessY)
                fatiguePath.moveTo(x, fatigueY)
                formPath.moveTo(x, formY)
            } else {
                fitnessPath.lineTo(x, fitnessY)
                fatiguePath.lineTo(x, fatigueY)
                formPath.lineTo(x, formY)
            }

            if (index % (points.size / 3).coerceAtLeast(1) == 0 || index == points.size - 1) {
                val date = LocalDate.parse(point.date)
                val monthNames = if (isSpanish) getSpanishMonths() else getEnglishMonths()
                val dateLabel = "${monthNames[date.month]} ${date.dayOfMonth}"
                val textLayoutResult = textMeasurer.measure(dateLabel, style = labelTextStyle)
                
                val adjustedX = when (index) {
                    0 -> x
                    points.size - 1 -> x - textLayoutResult.size.width
                    else -> x - (textLayoutResult.size.width / 2)
                }
                
                drawText(
                    textMeasurer = textMeasurer,
                    text = dateLabel,
                    style = labelTextStyle,
                    topLeft = Offset(adjustedX, size.height - textLayoutResult.size.height)
                )
            }
        }

        drawPath(path = fatiguePath, color = fatigueColor, style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round))
        drawPath(path = fitnessPath, color = fitnessColor, style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round))
        drawPath(path = formPath, color = formColor, style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round))

        // Draw Projection
        data.projection?.let { proj ->
            val lastHistoricalX = leftPadding + ((points.size - 1) * stepX)
            val lastHistoricalFitnessY = topPadding + chartHeight - ((points.last().fitness - minVal) / range) * chartHeight
            val lastHistoricalFatigueY = topPadding + chartHeight - ((points.last().fatigue - minVal) / range) * chartHeight
            val lastHistoricalFormY = topPadding + chartHeight - ((points.last().form - minVal) / range) * chartHeight

            val fitnessProjPath = Path()
            val fatigueProjPath = Path()
            val formProjPath = Path()

            fitnessProjPath.moveTo(lastHistoricalX, lastHistoricalFitnessY)
            fatigueProjPath.moveTo(lastHistoricalX, lastHistoricalFatigueY)
            formProjPath.moveTo(lastHistoricalX, lastHistoricalFormY)

            proj.forEachIndexed { index, point ->
                val x = lastHistoricalX + ((index + 1) * stepX)
                val fitnessY = topPadding + chartHeight - ((point.fitness - minVal) / range) * chartHeight
                val fatigueY = topPadding + chartHeight - ((point.fatigue - minVal) / range) * chartHeight
                val formY = topPadding + chartHeight - ((point.form - minVal) / range) * chartHeight

                fitnessProjPath.lineTo(x, fitnessY)
                fatigueProjPath.lineTo(x, fatigueY)
                formProjPath.lineTo(x, formY)
            }

            val dashEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            drawPath(path = fatigueProjPath, color = fatigueColor.copy(alpha = 0.6f), style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, pathEffect = dashEffect))
            drawPath(path = fitnessProjPath, color = fitnessColor.copy(alpha = 0.6f), style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, pathEffect = dashEffect))
            drawPath(path = formProjPath, color = formColor.copy(alpha = 0.6f), style = Stroke(width = 1.5.dp.toPx(), cap = StrokeCap.Round, pathEffect = dashEffect))
        }

        touchX?.let { tx ->
            val clampedX = tx.coerceIn(leftPadding, leftPadding + chartWidth)
            val index = ((clampedX - leftPadding) / stepX).roundToInt().coerceIn(0, points.size - 1)
            val point = points[index]
            val actualX = leftPadding + (index * stepX)
            
            onPointSelected(point)

            drawLine(
                color = labelColor.copy(alpha = 0.5f),
                start = Offset(actualX, topPadding),
                end = Offset(actualX, topPadding + chartHeight),
                strokeWidth = 1.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
            )

            drawPointHighlight(actualX, topPadding + chartHeight - ((point.fitness - minVal) / range) * chartHeight, fitnessColor)
            drawPointHighlight(actualX, topPadding + chartHeight - ((point.fatigue - minVal) / range) * chartHeight, fatigueColor)
            drawPointHighlight(actualX, topPadding + chartHeight - ((point.form - minVal) / range) * chartHeight, formColor)
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawPointHighlight(x: Float, y: Float, color: Color) {
    drawCircle(Color.White, radius = 4.dp.toPx(), center = Offset(x, y))
    drawCircle(color, radius = 4.dp.toPx(), center = Offset(x, y), style = Stroke(2.dp.toPx()))
}

private fun getSpanishMonths() = mapOf(
    Month.JANUARY to "Ene", Month.FEBRUARY to "Feb", Month.MARCH to "Mar",
    Month.APRIL to "Abr", Month.MAY to "May", Month.JUNE to "Jun",
    Month.JULY to "Jul", Month.AUGUST to "Ago", Month.SEPTEMBER to "Sep",
    Month.OCTOBER to "Oct", Month.NOVEMBER to "Nov", Month.DECEMBER to "Dic"
)

private fun getEnglishMonths() = Month.entries.associateWith { it.name.take(3) }

private val CircleShape = RoundedCornerShape(50)
