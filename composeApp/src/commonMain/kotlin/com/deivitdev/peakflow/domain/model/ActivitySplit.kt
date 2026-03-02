package com.deivitdev.peakflow.domain.model

data class ActivitySplit(
    val distanceKm: Float,
    val movingTimeSeconds: Int,
    val elevationDifference: Float,
    val averageSpeedKmh: Float,
    val averageHeartRate: Float?
)
