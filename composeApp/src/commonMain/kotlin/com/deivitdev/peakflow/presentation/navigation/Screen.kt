package com.deivitdev.peakflow.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screen(val title: String, val icon: ImageVector) {
    Workouts("Workouts", Icons.AutoMirrored.Filled.List),
    Performance("Performance", Icons.Default.BarChart),
    Insights("Insights", Icons.Default.Insights),
    Profile("Profile", Icons.Default.Person),
    ActivityDetail("Detail", Icons.AutoMirrored.Filled.List)
}
