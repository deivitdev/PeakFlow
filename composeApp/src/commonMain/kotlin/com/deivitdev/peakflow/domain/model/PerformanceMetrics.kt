package com.deivitdev.peakflow.domain.model

data class PerformanceMetrics(
    val totalDistanceKm: Float,
    val totalDurationSeconds: Int,
    val totalElevationGainMeters: Float,
    val averageSpeedKmh: Float,
    val activityCount: Int,
    val dominantType: WorkoutType,
    val breakdown: Map<WorkoutType, ActivityTypeMetrics>,
    val goalProgress: Float,
    val goalStatus: String
)
