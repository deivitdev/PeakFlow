package com.deivitdev.peakflow.presentation.analytics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deivitdev.peakflow.domain.model.ActivityTypeMetrics
import com.deivitdev.peakflow.domain.model.WorkoutType
import com.deivitdev.peakflow.presentation.components.SectionTitle
import com.deivitdev.peakflow.presentation.utils.formatDecimal
import com.deivitdev.peakflow.presentation.utils.formatSeconds
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*

@Composable
fun ActivityTypeBreakdownCard(
    breakdown: Map<WorkoutType, ActivityTypeMetrics>,
    totalDistance: Float,
    totalDuration: Int,
    modifier: Modifier = Modifier
) {
    if (breakdown.isEmpty()) return

    Column(modifier = modifier) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
        ) {
            Column(Modifier.padding(16.dp)) {
                breakdown.entries.sortedByDescending { it.value.totalDistanceKm }.forEachIndexed { index, entry ->
                    val workoutType = entry.key
                    val metrics = entry.value

                    val percentage = when (workoutType) {
                        WorkoutType.STRENGTH -> if (totalDuration > 0) metrics.totalDurationSeconds.toFloat() / totalDuration else 0f
                        else -> if (totalDistance > 0) metrics.totalDistanceKm / totalDistance else 0f
                    }

                    if (index > 0) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Icon and Name
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            Icon(
                                imageVector = getIconForWorkoutType(workoutType),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = getNameForWorkoutType(workoutType).uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        // Metrics
                        Column(horizontalAlignment = Alignment.End) {
                            if (workoutType != WorkoutType.STRENGTH) {
                                Text(
                                    text = "${formatDecimal(metrics.totalDistanceKm)} ${stringResource(Res.string.km)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = formatSeconds(metrics.totalDurationSeconds),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { percentage },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp)
                            .clip(RoundedCornerShape(2.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                        strokeCap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

@Composable
private fun getIconForWorkoutType(workoutType: WorkoutType): ImageVector {
    return when (workoutType) {
        is WorkoutType.CYCLING -> Icons.AutoMirrored.Filled.DirectionsBike
        is WorkoutType.RUNNING -> Icons.AutoMirrored.Filled.DirectionsRun
        WorkoutType.STRENGTH -> Icons.Filled.FitnessCenter
        WorkoutType.WALKING -> Icons.AutoMirrored.Filled.DirectionsWalk
        WorkoutType.OTHER -> Icons.Filled.QuestionMark
    }
}

@Composable
private fun getNameForWorkoutType(workoutType: WorkoutType): String {
    return when (workoutType) {
        WorkoutType.STRENGTH -> stringResource(Res.string.strength)
        WorkoutType.WALKING -> stringResource(Res.string.walking)
        is WorkoutType.RUNNING -> stringResource(Res.string.running)
        WorkoutType.OTHER -> stringResource(Res.string.other)
        is WorkoutType.CYCLING.ROAD -> stringResource(Res.string.road_cycling)
        is WorkoutType.CYCLING.MOUNTAIN -> stringResource(Res.string.mountain_cycling)
        is WorkoutType.CYCLING.GRAVEL -> stringResource(Res.string.gravel_cycling)
        is WorkoutType.CYCLING.GENERIC -> stringResource(Res.string.cycling)
        is WorkoutType.RUNNING.ROAD -> stringResource(Res.string.running)
        is WorkoutType.RUNNING.TRAIL -> stringResource(Res.string.running)
        is WorkoutType.RUNNING.GENERIC -> stringResource(Res.string.running)
    }
}
