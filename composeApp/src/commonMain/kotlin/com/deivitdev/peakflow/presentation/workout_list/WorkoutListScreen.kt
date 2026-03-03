package com.deivitdev.peakflow.presentation.workout_list

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
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
import com.deivitdev.peakflow.presentation.components.SectionTitle
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*

import com.deivitdev.peakflow.presentation.utils.formatDecimal
import com.deivitdev.peakflow.presentation.utils.formatSeconds

@Composable
fun WorkoutListScreen(
    viewModel: WorkoutListViewModel,
    onActivityClick: (String, WorkoutType) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Search and Filter Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(Res.string.search_workouts)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = if (uiState.searchQuery.isNotEmpty()) {
                    {
                        IconButton(onClick = { viewModel.onSearchQueryChange("") }) {
                            Icon(Icons.Default.Clear, contentDescription = "Clear search")
                        }
                    }
                } else null,
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            val categories = listOf(
                null to stringResource(Res.string.all_categories),
                WorkoutType.CYCLING.GENERIC to stringResource(Res.string.cycling),
                WorkoutType.RUNNING.GENERIC to stringResource(Res.string.running),
                WorkoutType.WALKING to stringResource(Res.string.walking),
                WorkoutType.STRENGTH to stringResource(Res.string.strength)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 4.dp)
            ) {
                items(categories.size) { index ->
                    val (type, label) = categories[index]
                    val isSelected = (uiState.selectedCategory == null && type == null) || 
                                   (uiState.selectedCategory != null && type != null && 
                                    uiState.selectedCategory!!::class == type::class)
                    
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.onCategorySelect(type) },
                        label = { Text(label.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        shape = RoundedCornerShape(8.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                            selectedLabelColor = MaterialTheme.colorScheme.primary
                        ),
                        border = if (!isSelected) BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)) else null
                    )
                }
            }
        }

        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.refreshActivities() },
            modifier = Modifier.fillMaxSize()
        ) {
            if (uiState.isLoading && uiState.activities.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    contentPadding = PaddingValues(top = 4.dp, bottom = 24.dp)
                ) {
                    items(uiState.activities) { activity ->
                        WorkoutItem(activity, onClick = { onActivityClick(activity.id, activity.type) })
                    }
                }
            }
        }
    }
}

@Composable
fun WorkoutItem(activity: Activity, onClick: () -> Unit) {
    val accentColor = MaterialTheme.colorScheme.primary
    val isDark = isSystemInDarkTheme()

    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header Row: Type Icon & Name
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = when (activity.type) {
                        is WorkoutType.CYCLING -> Icons.AutoMirrored.Filled.DirectionsBike
                        is WorkoutType.RUNNING -> Icons.AutoMirrored.Filled.DirectionsRun
                        WorkoutType.STRENGTH -> Icons.Default.FitnessCenter
                        WorkoutType.WALKING -> Icons.AutoMirrored.Filled.DirectionsWalk
                        else -> Icons.Default.QuestionMark
                    },
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = accentColor
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = activity.name.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = activity.startDate.take(10),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Large Hero Data Row
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.fillMaxWidth()
            ) {
                val heroValue = if (activity.type is WorkoutType.STRENGTH)
                    formatSeconds(activity.movingTimeSeconds)
                else
                    formatDecimal(activity.distanceKm)

                Text(
                    text = heroValue,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onSurface,
                    letterSpacing = (-1).sp
                )
                
                if (activity.type !is WorkoutType.STRENGTH) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(Res.string.km).uppercase(),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = accentColor,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), 
                thickness = 0.5.dp
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Sub-metrics Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SubMetric(stringResource(Res.string.total_time), formatSeconds(activity.movingTimeSeconds))
                if (activity.type !is WorkoutType.STRENGTH) {
                    SubMetric(stringResource(Res.string.elevation_gain), "${activity.totalElevationGainMeters.toInt()}${stringResource(Res.string.meters).uppercase()}")
                    SubMetric(stringResource(Res.string.avg_speed), "${activity.averageSpeedKmh.toInt()}${stringResource(Res.string.kmh).uppercase()}")
                } else {
                    SubMetric(stringResource(Res.string.intensity), stringResource(Res.string.pro))
                    SubMetric(stringResource(Res.string.load), stringResource(Res.string.heavy))
                }
            }
        }
    }
}

@Composable
private fun SubMetric(label: String, value: String) {
    Column {
        Text(
            text = label.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            letterSpacing = 0.5.sp
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
