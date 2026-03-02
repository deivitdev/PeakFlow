package com.deivitdev.peakflow.presentation.activity_detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.model.HeartRateZone
import com.deivitdev.peakflow.presentation.components.SectionTitle
import com.deivitdev.peakflow.presentation.components.MetricItem
import com.deivitdev.peakflow.presentation.components.SplitsList
import com.deivitdev.peakflow.presentation.components.GearCard
import com.deivitdev.peakflow.presentation.utils.formatDecimal
import com.deivitdev.peakflow.presentation.utils.formatSeconds
import com.deivitdev.peakflow.presentation.utils.formatPace
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk

import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Star

@Composable
fun RunningDetailContent(
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
    showCadence: Boolean,
    onCadToggle: () -> Unit,
    onMapClick: (String) -> Unit,
    decoupling: Float? = null,
    aerobicStatus: com.deivitdev.peakflow.domain.model.AerobicStatus = com.deivitdev.peakflow.domain.model.AerobicStatus.INSUFFICIENT_DATA,
    avgGapKmh: Float? = null
) {
    ActivityHeader(activity, userName, userPhotoUrl, deviceName = activity.deviceName)

    Spacer(modifier = Modifier.height(32.dp))

    SectionTitle(stringResource(Res.string.athlete_data))
    
    // RUNNING BENTO CARD
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth()) {
                MetricItem(stringResource(Res.string.total_distance), formatDecimal(activity.distanceKm), stringResource(Res.string.km), Modifier.weight(1f))
                MetricItem(
                    stringResource(Res.string.avg_pace), 
                    formatPace(activity.averageSpeedKmh), 
                    stringResource(Res.string.min_km), 
                    Modifier.weight(1f),
                    subMetrics = listOfNotNull(
                        activity.maxSpeedKmh.let { stringResource(Res.string.max_pace) to formatPace(it) },
                        avgGapKmh?.let { stringResource(Res.string.avg_gap) to formatPace(it) }
                    )
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            Row(Modifier.fillMaxWidth()) {
                MetricItem(stringResource(Res.string.moving_time), formatSeconds(activity.movingTimeSeconds), "", Modifier.weight(1f))
                
                val cadence = activity.cadenceSeries?.average()?.toInt()
                MetricItem(
                    stringResource(Res.string.cadence), 
                    cadence?.toString() ?: "--", 
                    stringResource(Res.string.spm), 
                    Modifier.weight(1f)
                )
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
            Row(Modifier.fillMaxWidth()) {
                MetricItem(stringResource(Res.string.elevation_gain), "${activity.totalElevationGainMeters.toInt()}", stringResource(Res.string.meters).uppercase(), Modifier.weight(1f))
                MetricItem(stringResource(Res.string.calories_label), activity.calories?.toInt()?.toString() ?: "--", stringResource(Res.string.kcal).uppercase(), Modifier.weight(1f))
            }
        }
    }

    // 1.5 Best Efforts
    if (!activity.bestEfforts.isNullOrEmpty()) {
        Spacer(modifier = Modifier.height(32.dp))
        SectionTitle(stringResource(Res.string.best_efforts_title))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        ) {
            Column(Modifier.padding(16.dp)) {
                activity.bestEfforts.forEachIndexed { index, effort ->
                    if (index > 0) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.1f))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MilitaryTech, 
                                contentDescription = null, 
                                tint = Color(0xFFFFD600),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = effort.name.uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        Text(
                            text = formatSeconds(effort.movingTimeSeconds),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }

    // 2. Hydration & Efficiency
    Spacer(modifier = Modifier.height(32.dp))
    SectionTitle(stringResource(Res.string.hydration_title))
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                // Heuristic: 0.8L per hour at IF 1.0
                val durationHours = activity.movingTimeSeconds / 3600f
                // We don't have IF here easily without recalculating, but we can approximate or just use a base rate
                val estSweat = durationHours * 0.75f 
                
                MetricItem(
                    stringResource(Res.string.est_sweat_loss), 
                    "%.2f".format(estSweat), 
                    stringResource(Res.string.liters), 
                    Modifier.weight(1f)
                )
                
                val restTime = (activity.elapsedTimeSeconds - activity.movingTimeSeconds).coerceAtLeast(0)
                MetricItem(stringResource(Res.string.rest_time), formatSeconds(restTime), "", Modifier.weight(1f))
            }
        }
    }

    if (aerobicStatus != com.deivitdev.peakflow.domain.model.AerobicStatus.INSUFFICIENT_DATA) {
        Spacer(modifier = Modifier.height(32.dp))
        AerobicEfficiencyCard(decoupling = decoupling, status = aerobicStatus)
    }

    if (!activity.gearName.isNullOrBlank()) {
        Spacer(modifier = Modifier.height(32.dp))
        SectionTitle(stringResource(Res.string.shoes))
        GearCard(
            gearName = activity.gearName, 
            gearDistance = activity.gearDistance,
            icon = Icons.AutoMirrored.Filled.DirectionsRun
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
        showCadenceFilter = true,
        showHR = showHeartRate,
        onHRToggle = onHRToggle,
        showElev = showElevation,
        onElevToggle = onElevToggle,
        showCad = showCadence,
        onCadToggle = onCadToggle,
        cadenceLabel = stringResource(Res.string.spm)
    )

    PerformanceChartCard(
        activity = activity,
        showHeartRate = showHeartRate,
        showElevation = showElevation,
        showCadence = showCadence,
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
