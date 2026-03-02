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

@Composable
fun GenericDetailContent(
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
    onMapClick: (String) -> Unit
) {
    ActivityHeader(activity, userName, userPhotoUrl, deviceName = activity.deviceName)

    Spacer(modifier = Modifier.height(32.dp))

    SectionTitle(stringResource(Res.string.athlete_data), Modifier.padding(bottom = 12.dp))
    
    // INTEGRATED BENTO CARD for generic metrics
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth()) {
                MetricItem(stringResource(Res.string.total_distance), formatDecimal(activity.distanceKm), stringResource(Res.string.km), Modifier.weight(1f))
                MetricItem(stringResource(Res.string.moving_time), formatSeconds(activity.movingTimeSeconds), "", Modifier.weight(1f))
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            Row(Modifier.fillMaxWidth()) {
                MetricItem(stringResource(Res.string.elevation_gain), "${activity.totalElevationGainMeters.toInt()}", stringResource(Res.string.meters).uppercase(), Modifier.weight(1f))
                MetricItem(stringResource(Res.string.avg_speed), "${activity.averageSpeedKmh.toInt()}", stringResource(Res.string.kmh).uppercase(), Modifier.weight(1f))
            }
        }
    }

    if (!activity.polyline.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(32.dp))
        SectionTitle(stringResource(Res.string.route_map), Modifier.padding(bottom = 12.dp))
        MapThumbnailCard(
            polyline = activity.polyline,
            onClick = { onMapClick(activity.polyline) }
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    if (activity.heartRateSeries != null || activity.elevationSeries != null) {
        SectionTitle(stringResource(Res.string.performance_telemetry), Modifier.padding(bottom = 12.dp))
        
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
    }

    if (hrZones.isNotEmpty()) {
        Spacer(modifier = Modifier.height(32.dp))
        SectionTitle(stringResource(Res.string.training_zones), Modifier.padding(bottom = 12.dp))
        ZonesCard(zones = hrZones)
    }

    if (activity.splits != null && activity.splits.isNotEmpty()) {
        Spacer(modifier = Modifier.height(32.dp))
        SectionTitle(stringResource(Res.string.splits_title), Modifier.padding(bottom = 12.dp))
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


