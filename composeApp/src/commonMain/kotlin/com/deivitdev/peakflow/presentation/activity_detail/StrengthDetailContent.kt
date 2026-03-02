package com.deivitdev.peakflow.presentation.activity_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.model.HeartRateZone
import com.deivitdev.peakflow.presentation.components.SectionTitle
import com.deivitdev.peakflow.presentation.components.MetricItem
import com.deivitdev.peakflow.presentation.components.MetricInfoTooltip
import com.deivitdev.peakflow.presentation.utils.formatSeconds
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*

@Composable
fun StrengthDetailContent(
    activity: Activity,
    userName: String?,
    userPhotoUrl: String?,
    hrZones: List<HeartRateZone>,
    hoveredIndex: Int?,
    onHoverChanged: (Int?) -> Unit,
    showHeartRate: Boolean,
    onHRToggle: () -> Unit
) {
    ActivityHeader(activity, userName, userPhotoUrl, deviceName = activity.deviceName)

    Spacer(modifier = Modifier.height(32.dp))

    // 1. INTENSITY / RELATIVE EFFORT CARD
    if (activity.sufferScore != null) {
        SectionTitle(stringResource(Res.string.relative_effort))
        RelativeEffortCard(score = activity.sufferScore)
        Spacer(modifier = Modifier.height(32.dp))
    }

    SectionTitle(stringResource(Res.string.athlete_data))
    
    // 2. ENHANCED BENTO CARD
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(16.dp)) {
            // Row 1: Time Analysis
            Row(Modifier.fillMaxWidth()) {
                MetricItem(stringResource(Res.string.moving_time), formatSeconds(activity.movingTimeSeconds), "", Modifier.weight(1f))
                MetricItem(stringResource(Res.string.elapsed_time), formatSeconds(activity.elapsedTimeSeconds), "", Modifier.weight(1f))
            }
            
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            
            // Row 2: Calories and Rest
            Row(Modifier.fillMaxWidth()) {
                MetricItem(stringResource(Res.string.calories_label), activity.calories?.toInt()?.toString() ?: "--", stringResource(Res.string.kcal).uppercase(), Modifier.weight(1f))
                val restTime = (activity.elapsedTimeSeconds - activity.movingTimeSeconds).coerceAtLeast(0)
                MetricItem(stringResource(Res.string.rest_time), formatSeconds(restTime), "", Modifier.weight(1f))
            }

            if (activity.averageHeartRate != null || activity.maxHeartRate != null) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                Row(Modifier.fillMaxWidth()) {
                    if (activity.averageHeartRate != null) {
                        MetricItem(stringResource(Res.string.avg_hr), "${activity.averageHeartRate.toInt()}", stringResource(Res.string.bpm), Modifier.weight(1f))
                    }
                    if (activity.maxHeartRate != null) {
                        MetricItem(stringResource(Res.string.max_hr), "${activity.maxHeartRate.toInt()}", stringResource(Res.string.bpm), Modifier.weight(1f))
                    }
                }
            }
        }
    }

    if (hrZones.isNotEmpty()) {
        Spacer(modifier = Modifier.height(32.dp))
        SectionTitle(stringResource(Res.string.training_zones))
        ZonesCard(zones = hrZones)
    }

    if (activity.heartRateSeries != null && activity.heartRateSeries.isNotEmpty()) {
        Spacer(modifier = Modifier.height(32.dp))
        SectionTitle(stringResource(Res.string.performance_telemetry))
        
        ChartFilters(
            showCadenceFilter = false,
            showHR = showHeartRate,
            onHRToggle = onHRToggle,
            showElev = false,
            onElevToggle = {},
            showCad = false,
            onCadToggle = {}
        )

        PerformanceChartCard(
            activity = activity,
            showHeartRate = showHeartRate,
            showElevation = false,
            showCadence = false,
            hoveredIndex = hoveredIndex,
            onHoverChanged = onHoverChanged
        )
    }
}

@Composable
private fun RelativeEffortCard(score: Int) {
    val color = com.deivitdev.peakflow.presentation.utils.getIntensityColor(score)
    val label = when {
        score < 15 -> stringResource(Res.string.intensity_easy)
        score < 30 -> stringResource(Res.string.intensity_moderate)
        score < 60 -> stringResource(Res.string.intensity_hard)
        else -> stringResource(Res.string.intensity_extreme)
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
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = color,
                    letterSpacing = (-0.5).sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.strava_relative_effort),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    MetricInfoTooltip(acronym = stringResource(Res.string.strava_relative_effort))
                }
            }
            
            Surface(
                color = color.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
            ) {
                Text(
                    text = score.toString(),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = color
                )
            }
        }
    }
}
