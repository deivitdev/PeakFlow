package com.deivitdev.peakflow.presentation.activity_detail

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.model.WorkoutType
import com.deivitdev.peakflow.presentation.components.InteractiveMultiTelemetryChart
import com.deivitdev.peakflow.presentation.components.TelemetrySeries
import com.deivitdev.peakflow.presentation.components.ZonesDistributionChart
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*
import coil3.compose.AsyncImage

import androidx.compose.foundation.clickable
import com.deivitdev.peakflow.presentation.components.InteractiveMap

import com.deivitdev.peakflow.domain.model.AerobicStatus

@Composable
fun AerobicEfficiencyCard(
    decoupling: Float?,
    status: AerobicStatus
) {
    if (decoupling == null || status == AerobicStatus.INSUFFICIENT_DATA) return

    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    val statusColor = when (status) {
        AerobicStatus.STABLE -> Color(0xFF4CAF50) // Green
        AerobicStatus.DRIFT_DETECTED -> if (decoupling > 10f) Color(0xFFF44336) else Color(0xFFFF9800) // Red or Orange
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    val statusLabel = when (status) {
        AerobicStatus.STABLE -> stringResource(Res.string.status_stable)
        AerobicStatus.DRIFT_DETECTED -> stringResource(Res.string.status_drift)
        else -> ""
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.aerobic_efficiency).uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = statusLabel.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = statusColor
                )
                Text(
                    text = stringResource(Res.string.steady_state_info),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                    fontSize = 8.sp,
                    lineHeight = 10.sp
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "DECOUPLING",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
                Text(
                    text = "${if (decoupling >= 0) "+" else ""}${decoupling.toInt()}%",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                    color = statusColor
                )
            }
        }
    }
}

@Composable
fun MapThumbnailCard(
    polyline: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            InteractiveMap(
                polyline = polyline,
                isInteractive = false,
                showMarkers = false,
                modifier = Modifier.fillMaxSize()
            )
            // Transparent overlay to catch the click reliably
            Box(
                Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .clickable { onClick() }
            )
        }
    }
}



@Composable
fun ActivityHeader(activity: Activity, userName: String?, userPhotoUrl: String?, deviceName: String?) {
    val accentColor = MaterialTheme.colorScheme.primary
    val isDark = androidx.compose.foundation.isSystemInDarkTheme()
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (!userPhotoUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = userPhotoUrl,
                            contentDescription = "Profile Photo",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = androidx.compose.ui.layout.ContentScale.Crop
                        )
                    } else {
                        DetailIcon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, size = 24.dp)
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = (userName ?: "ATHLETE").uppercase(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp,
                        color = accentColor
                    )
                    if (activity.gearName != null) {
                        Text(
                            text = activity.gearName.uppercase(),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            letterSpacing = 0.5.sp
                        )
                    }
                    Text(
                        text = activity.startDate,
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = activity.name.uppercase(),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 28.sp
            )
            
            deviceName?.let {
                Text(
                    text = it.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            
            activity.location?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    DetailIcon(Icons.Default.Place, contentDescription = null, size = 14.dp, tint = accentColor)
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = it.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = accentColor.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
fun DetailIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, contentDescription: String?, size: androidx.compose.ui.unit.Dp, tint: Color) {
    androidx.compose.material3.Icon(icon, contentDescription, modifier = Modifier.size(size), tint = tint)
}

@Composable
fun DetailHeroCard(
    label: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier,
    hasPowerMeter: Boolean = false,
    subMetrics: List<Pair<String, String>> = emptyList()
) {
    val accentColor = MaterialTheme.colorScheme.primary
    
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp,
                    modifier = Modifier.weight(1f)
                )
                if (hasPowerMeter) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(accentColor.copy(alpha = 0.1f))
                            .padding(horizontal = 4.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "SENSOR",
                            style = MaterialTheme.typography.labelSmall,
                            color = accentColor,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace
                )
                if (unit.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = unit,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }

            if (subMetrics.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    subMetrics.forEach { (subLabel, subValue) ->
                        Column {
                            Text(
                                text = subLabel.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 8.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Text(
                                text = subValue,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PerformanceChartCard(
    activity: Activity,
    showHeartRate: Boolean,
    showElevation: Boolean,
    showCadence: Boolean,
    hoveredIndex: Int?,
    onHoverChanged: (Int?) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(vertical = 16.dp)) {
            val series = mutableListOf<TelemetrySeries>()
            val isDark = androidx.compose.foundation.isSystemInDarkTheme()
            val cadenceColor = if (isDark) Color(0xFFFFD600) else Color(0xFFFFA000)
            
            Box(Modifier.padding(horizontal = 8.dp)) {
                if (showHeartRate) {
                    activity.heartRateSeries?.let {
                        series.add(TelemetrySeries(it.map { v -> v.toFloat() }, MaterialTheme.colorScheme.tertiary, stringResource(Res.string.hr_short), "HR"))
                    }
                }
                if (showElevation) {
                    activity.elevationSeries?.let {
                        series.add(TelemetrySeries(it, MaterialTheme.colorScheme.primary, stringResource(Res.string.elev_short), "ELEV"))
                    }
                }
                if (showCadence && (activity.type is WorkoutType.CYCLING || activity.type is WorkoutType.RUNNING)) {
                    val label = if (activity.type is WorkoutType.CYCLING) stringResource(Res.string.rpm) else stringResource(Res.string.spm)
                    activity.cadenceSeries?.let {
                        series.add(TelemetrySeries(it.map { v -> v.toFloat() }, cadenceColor, label.uppercase(), "CAD"))
                    }
                }

                InteractiveMultiTelemetryChart(
                    seriesList = series,
                    hoveredIndex = hoveredIndex,
                    onHoverChanged = onHoverChanged,
                    modifier = Modifier.height(240.dp),
                    overlayContent = {} 
                )

                // INTEGRATED TELEMETRY OVERLAY (Appears ON TOP of the chart)
                androidx.compose.animation.AnimatedVisibility(
                    visible = hoveredIndex != null,
                    enter = fadeIn() + slideInVertically { -it },
                    exit = fadeOut() + slideOutVertically { -it },
                    modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 0.dp )
                ) {
                    val isCycling = activity.type is WorkoutType.CYCLING
                    IntegratedTelemetryCard(
                        heartRate = if (showHeartRate) activity.heartRateSeries?.getOrNull(hoveredIndex ?: -1) else null,
                        elevation = if (showElevation) activity.elevationSeries?.getOrNull(hoveredIndex ?: -1) else null,
                        cadence = if (showCadence && (isCycling || activity.type is WorkoutType.RUNNING)) activity.cadenceSeries?.getOrNull(hoveredIndex ?: -1) else null,
                        cadenceUnit = if (isCycling) stringResource(Res.string.rpm) else stringResource(Res.string.spm),
                        cadenceColor = cadenceColor,
                        modifier = Modifier.wrapContentWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun IntegratedTelemetryCard(
    heartRate: Int?,
    elevation: Float?,
    cadence: Int?,
    cadenceUnit: String,
    cadenceColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        ),
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val metrics = mutableListOf<@Composable () -> Unit>()
            
            if (heartRate != null) {
                metrics.add {
                    HudMetric(
                        label = stringResource(Res.string.heart_rate),
                        value = heartRate.toString(),
                        unit = stringResource(Res.string.bpm),
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
            
            if (elevation != null) {
                metrics.add {
                    HudMetric(
                        label = stringResource(Res.string.elevation),
                        value = elevation.toInt().toString(),
                        unit = stringResource(Res.string.meters).uppercase(),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            if (cadence != null) {
                metrics.add {
                    HudMetric(
                        label = stringResource(Res.string.cadence),
                        value = cadence.toString(),
                        unit = cadenceUnit,
                        color = cadenceColor
                    )
                }
            }

            metrics.forEachIndexed { index, metric ->
                metric()
                if (index < metrics.lastIndex) {
                    Spacer(modifier = Modifier.width(16.dp))
                    VerticalDivider(
                        modifier = Modifier.height(24.dp), 
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}

@Composable
fun HudMetric(label: String, value: String, unit: String, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp) // More space between label and value
    ) {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontSize = 7.sp,
            color = color,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp // More air between characters in label
        )
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp // Slightly more air for the numerical value
            )
            Spacer(Modifier.width(2.dp))
            Text(
                text = unit,
                style = MaterialTheme.typography.labelSmall,
                fontSize = 7.sp,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun ZonesCard(zones: List<com.deivitdev.peakflow.domain.model.HeartRateZone>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
    ) {
        Box(Modifier.padding(20.dp)) {
            ZonesDistributionChart(zones = zones)
        }
    }
}

@Composable
fun ChartFilters(
    showCadenceFilter: Boolean,
    showHR: Boolean,
    onHRToggle: () -> Unit,
    showElev: Boolean,
    onElevToggle: () -> Unit,
    showCad: Boolean,
    onCadToggle: () -> Unit,
    cadenceLabel: String = stringResource(Res.string.cadence)
) {
    Row(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
        FilterChip(
            selected = showHR,
            onClick = onHRToggle,
            label = { Text(stringResource(Res.string.heart_rate).uppercase(), fontSize = 10.sp) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f),
                selectedLabelColor = MaterialTheme.colorScheme.tertiary
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterChip(
            selected = showElev,
            onClick = onElevToggle,
            label = { Text(stringResource(Res.string.elevation).uppercase(), fontSize = 10.sp) },
            colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                selectedLabelColor = MaterialTheme.colorScheme.primary
            )
        )
        if (showCadenceFilter) {
            Spacer(modifier = Modifier.width(8.dp))
            val cadenceSelectedColor = if (androidx.compose.foundation.isSystemInDarkTheme()) Color(0xFFFFD600) else Color(0xFFFFA000)
            FilterChip(
                selected = showCad,
                onClick = onCadToggle,
                label = { Text(cadenceLabel.uppercase(), fontSize = 10.sp) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = Color(0xFFFFD600).copy(alpha = 0.2f),
                    selectedLabelColor = cadenceSelectedColor
                )
            )
        }
    }
}


