package com.deivitdev.peakflow.presentation.activity_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.model.HeartRateZone
import com.deivitdev.peakflow.presentation.components.SplitsList
import com.deivitdev.peakflow.presentation.components.SectionTitle
import com.deivitdev.peakflow.presentation.components.MetricItem
import com.deivitdev.peakflow.presentation.utils.formatDecimal
import com.deivitdev.peakflow.presentation.utils.formatSeconds
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import com.deivitdev.peakflow.presentation.components.GearCard
import com.deivitdev.peakflow.presentation.utils.formatPace

@Composable
fun WalkingDetailContent(
    activity: Activity,
    userName: String?,
    userPhotoUrl: String?,
    hrZones: List<HeartRateZone>,
    hoveredIndex: Int?,
    onHoverChanged: (Int?) -> Unit,
    showHeartRate: Boolean,
    onHRToggle: () -> Unit,
    showElevation: Boolean,
    onElevToggle: () -> Unit,
    onMapClick: (String) -> Unit,
    decoupling: Float? = null,
    aerobicStatus: com.deivitdev.peakflow.domain.model.AerobicStatus = com.deivitdev.peakflow.domain.model.AerobicStatus.INSUFFICIENT_DATA
) {
    ActivityHeader(activity, userName, userPhotoUrl, deviceName = activity.deviceName)

    Spacer(modifier = Modifier.height(32.dp))

    SectionTitle(stringResource(Res.string.athlete_data))
    
    // INTEGRATED BENTO CARD for walking metrics
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth()) {
                MetricItem(stringResource(Res.string.total_distance), formatDecimal(activity.distanceKm), stringResource(Res.string.km), Modifier.weight(1f))
                MetricItem(stringResource(Res.string.avg_pace), formatPace(activity.averageSpeedKmh), stringResource(Res.string.min_km), Modifier.weight(1f))
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            Row(Modifier.fillMaxWidth()) {
                MetricItem(stringResource(Res.string.moving_time), formatSeconds(activity.movingTimeSeconds), "", Modifier.weight(1f))
                MetricItem(stringResource(Res.string.max_pace), formatPace(activity.maxSpeedKmh), stringResource(Res.string.min_km), Modifier.weight(1f))
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            Row(Modifier.fillMaxWidth()) {
                MetricItem(stringResource(Res.string.elevation_gain), "${activity.totalElevationGainMeters.toInt()}", stringResource(Res.string.meters).uppercase(), Modifier.weight(1f))
                MetricItem(stringResource(Res.string.calories_label), activity.calories?.toInt()?.toString() ?: "--", stringResource(Res.string.kcal).uppercase(), Modifier.weight(1f))
            }
        }
    }

    if (aerobicStatus != com.deivitdev.peakflow.domain.model.AerobicStatus.INSUFFICIENT_DATA) {
        Spacer(modifier = Modifier.height(32.dp))
        AerobicEfficiencyCard(decoupling = decoupling, status = aerobicStatus)
    }

    // 2. Environment & Elevation Detail
    if (activity.averageTemp != null || activity.elevHigh != null || activity.elevLow != null) {
        Spacer(modifier = Modifier.height(32.dp))
        SectionTitle(stringResource(Res.string.environment))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        ) {
            Column(Modifier.padding(16.dp)) {
                Row(Modifier.fillMaxWidth()) {
                    activity.averageTemp?.let { temp ->
                        MetricItem(stringResource(Res.string.avg_temp), "$temp", stringResource(Res.string.celsius), Modifier.weight(1f))
                    }
                    val restTime = (activity.elapsedTimeSeconds - activity.movingTimeSeconds).coerceAtLeast(0)
                    MetricItem(stringResource(Res.string.rest_time), formatSeconds(restTime), "", Modifier.weight(1f))
                }
                if (activity.elevHigh != null || activity.elevLow != null) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                    Row(Modifier.fillMaxWidth()) {
                        activity.elevHigh?.let { high ->
                            MetricItem(stringResource(Res.string.elev_high), "${high.toInt()}", stringResource(Res.string.meters).uppercase(), Modifier.weight(1f))
                        }
                        activity.elevLow?.let { low ->
                            MetricItem(stringResource(Res.string.elev_low), "${low.toInt()}", stringResource(Res.string.meters).uppercase(), Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }

    if (!activity.gearName.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(32.dp))
        SectionTitle(stringResource(Res.string.shoes))
        GearCard(
            gearName = activity.gearName, 
            gearDistance = activity.gearDistance,
            icon = Icons.AutoMirrored.Filled.DirectionsWalk
        )
    }

    if (!activity.polyline.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(32.dp))
        SectionTitle(stringResource(Res.string.route_map))
        MapThumbnailCard(
            polyline = activity.polyline,
            onClick = { onMapClick(activity.polyline) }
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    SectionTitle(stringResource(Res.string.performance_telemetry))
    
    ChartFilters(
        showCadenceFilter = false,
        showHR = showHeartRate,
        onHRToggle = onHRToggle,
        showElev = showElevation,
        onElevToggle = onElevToggle,
        showCad = false,
        onCadToggle = {}
    )

    PerformanceChartCard(
        activity = activity,
        showHeartRate = showHeartRate,
        showElevation = showElevation,
        showCadence = false,
        hoveredIndex = hoveredIndex,
        onHoverChanged = onHoverChanged
    )

    if (hrZones.isNotEmpty()) {
        Spacer(modifier = Modifier.height(32.dp))
        SectionTitle(stringResource(Res.string.training_zones))
        ZonesCard(zones = hrZones)
    }

    if (activity.splits != null && activity.splits.isNotEmpty()) {
        Spacer(modifier = Modifier.height(32.dp))
        SectionTitle(stringResource(Res.string.splits_title))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        ) {
            Box(Modifier.padding(16.dp)) {
                SplitsList(splits = activity.splits)
            }
        }
    }
}


