package com.deivitdev.peakflow.domain.model

data class ActivityTypeMetrics(
    val workoutType: WorkoutType,
    val count: Int,
    val totalDistanceKm: Float,
    val totalDurationSeconds: Int
)
