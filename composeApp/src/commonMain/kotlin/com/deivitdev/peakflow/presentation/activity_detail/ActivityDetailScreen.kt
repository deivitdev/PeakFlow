package com.deivitdev.peakflow.presentation.activity_detail

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.model.WorkoutType
import com.deivitdev.peakflow.presentation.components.AbstractBackdrop
import com.deivitdev.peakflow.presentation.components.InteractiveMap
import com.deivitdev.peakflow.presentation.activity_detail.RunningDetailContent
import com.deivitdev.peakflow.presentation.utils.formatDecimal
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*

@Composable
fun ActivityDetailScreen(
    id: String,
    type: WorkoutType? = null,
    viewModel: ActivityDetailViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val accentColor = MaterialTheme.colorScheme.primary

    var showHeartRate by remember { mutableStateOf(true) }
    var showElevation by remember { mutableStateOf(true) }
    var showCadence by remember { mutableStateOf(true) }
    var isMapExpanded by remember { mutableStateOf(false) }
    var hoveredIndex by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(id) {
        viewModel.loadActivity(id)
    }

    val activity = uiState.activity

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        // 1. MAIN CONTENT (Standard Integrated Layout)
        HudLayoutContainer(
            isLoading = uiState.isLoading,
            onBack = onBack,
            content = {
                if (activity != null) {
                    when (activity.type) {
                        is WorkoutType.CYCLING -> {
                            CyclingDetailContent(
                                activity = activity,
                                userName = uiState.userName,
                                userPhotoUrl = uiState.userPhotoUrl,
                                hrZones = uiState.hrZones,
                                hoveredIndex = hoveredIndex,
                                onHoverChanged = { hoveredIndex = it },
                                showHeartRate = showHeartRate,
                                onHRToggle = {
                                    val activeCount = (if (showHeartRate) 1 else 0) + (if (showElevation) 1 else 0) + (if (showCadence) 1 else 0)
                                    if (!showHeartRate || activeCount > 1) showHeartRate = !showHeartRate
                                },
                                showElevation = showElevation,
                                onElevToggle = {
                                    val activeCount = (if (showHeartRate) 1 else 0) + (if (showElevation) 1 else 0) + (if (showCadence) 1 else 0)
                                    if (!showElevation || activeCount > 1) showElevation = !showElevation
                                },
                                showCadence = showCadence,
                                onCadToggle = {
                                    val activeCount = (if (showHeartRate) 1 else 0) + (if (showElevation) 1 else 0) + (if (showCadence) 1 else 0)
                                    if (!showCadence || activeCount > 1) showCadence = !showCadence
                                },
                                onMapClick = { isMapExpanded = true },
                                ifValue = uiState.ifValue,
                                tssValue = uiState.tssValue,
                                decoupling = uiState.decouplingPercentage,
                                aerobicStatus = uiState.aerobicStatus,
                                avgWkg = uiState.avgWkg,
                                maxWkg = uiState.maxWkg
                            )
                        }
                        is WorkoutType.RUNNING -> {
                            RunningDetailContent(
                                activity = activity,
                                userName = uiState.userName,
                                userPhotoUrl = uiState.userPhotoUrl,
                                hrZones = uiState.hrZones,
                                hoveredIndex = hoveredIndex,
                                onHoverChanged = { hoveredIndex = it },
                                showHeartRate = showHeartRate,
                                onHRToggle = {
                                    val activeCount = (if (showHeartRate) 1 else 0) + (if (showElevation) 1 else 0) + (if (showCadence) 1 else 0)
                                    if (!showHeartRate || activeCount > 1) showHeartRate = !showHeartRate
                                },
                                showElevation = showElevation,
                                onElevToggle = {
                                    val activeCount = (if (showHeartRate) 1 else 0) + (if (showElevation) 1 else 0) + (if (showCadence) 1 else 0)
                                    if (!showElevation || activeCount > 1) showElevation = !showElevation
                                },
                                showCadence = showCadence,
                                onCadToggle = {
                                    val activeCount = (if (showHeartRate) 1 else 0) + (if (showElevation) 1 else 0) + (if (showCadence) 1 else 0)
                                    if (!showCadence || activeCount > 1) showCadence = !showCadence
                                },
                                onMapClick = { isMapExpanded = true },
                                decoupling = uiState.decouplingPercentage,
                                aerobicStatus = uiState.aerobicStatus,
                                avgGapKmh = uiState.avgGapKmh
                            )
                        }
                        WorkoutType.WALKING -> {
                            WalkingDetailContent(
                                activity = activity,
                                userName = uiState.userName,
                                userPhotoUrl = uiState.userPhotoUrl,
                                hrZones = uiState.hrZones,
                                hoveredIndex = hoveredIndex,
                                onHoverChanged = { hoveredIndex = it },
                                showHeartRate = showHeartRate,
                                onHRToggle = {
                                    val activeCount = (if (showHeartRate) 1 else 0) + (if (showElevation) 1 else 0)
                                    if (!showHeartRate || activeCount > 1) showHeartRate = !showHeartRate
                                },
                                showElevation = showElevation,
                                onElevToggle = {
                                    val activeCount = (if (showHeartRate) 1 else 0) + (if (showElevation) 1 else 0)
                                    if (!showElevation || activeCount > 1) showElevation = !showElevation
                                },
                                onMapClick = { isMapExpanded = true },
                                decoupling = uiState.decouplingPercentage,
                                aerobicStatus = uiState.aerobicStatus
                            )
                        }
                        WorkoutType.STRENGTH -> {
                            StrengthDetailContent(
                                activity = activity,
                                userName = uiState.userName,
                                userPhotoUrl = uiState.userPhotoUrl,
                                hrZones = uiState.hrZones,
                                hoveredIndex = hoveredIndex,
                                onHoverChanged = { hoveredIndex = it },
                                showHeartRate = showHeartRate,
                                onHRToggle = { showHeartRate = true }
                            )
                        }
                        WorkoutType.OTHER -> {
                            GenericDetailContent(
                                activity = activity,
                                userName = uiState.userName,
                                userPhotoUrl = uiState.userPhotoUrl,
                                hrZones = uiState.hrZones,
                                hoveredIndex = hoveredIndex,
                                onHoverChanged = { hoveredIndex = it },
                                showHeartRate = showHeartRate,
                                onHRToggle = {
                                    val activeCount = (if (showHeartRate) 1 else 0) + (if (showElevation) 1 else 0)
                                    if (!showHeartRate || activeCount > 1) showHeartRate = !showHeartRate
                                },
                                showElevation = showElevation,
                                onElevToggle = {
                                    val activeCount = (if (showHeartRate) 1 else 0) + (if (showElevation) 1 else 0)
                                    if (!showElevation || activeCount > 1) showElevation = !showElevation
                                },
                                onMapClick = { isMapExpanded = true }
                            )
                        }
                    }
                }
            }
        )

        // 2. MAP PORTAL (Overlay for expansion)
        AnimatedVisibility(
            visible = isMapExpanded && activity?.polyline != null,
            enter = fadeIn(animationSpec = tween(400)) + expandIn(animationSpec = tween(400), expandFrom = Alignment.Center),
            exit = fadeOut(animationSpec = tween(300)) + shrinkOut(animationSpec = tween(300), shrinkTowards = Alignment.Center)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                InteractiveMap(
                    polyline = activity!!.polyline!!,
                    isInteractive = true,
                    showMarkers = true,
                    modifier = Modifier.fillMaxSize()
                )
                
                // Floating Controls over the expanded map
                Box(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(16.dp)
                        .align(Alignment.TopStart)
                ) {
                    Surface(
                        onClick = { isMapExpanded = false },
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f)),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Close, contentDescription = "Close Map", tint = accentColor)
                        }
                    }
                }
            }
        }
    }
}
