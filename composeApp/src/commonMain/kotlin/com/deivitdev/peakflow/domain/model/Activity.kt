package com.deivitdev.peakflow.domain.model

data class Activity(
    val id: String,
    val name: String,
    val type: WorkoutType,
    val distanceKm: Float,
    val movingTimeSeconds: Int,
    val elapsedTimeSeconds: Int,
    val totalElevationGainMeters: Float,
    val startDate: String,
    val averageSpeedKmh: Float,
    val maxSpeedKmh: Float,
    val heartRateSeries: List<Int>? = null,
    val elevationSeries: List<Float>? = null,
    val averageHeartRate: Float? = null,
    val maxHeartRate: Float? = null,
    val calories: Float? = null,
    val kilojoules: Float? = null,
    val sufferScore: Int? = null,
    val averagePower: Float? = null,
    val location: String? = null,
    val polyline: String? = null,
    val cadenceSeries: List<Int>? = null,
    val velocitySmoothSeries: List<Float>? = null,
    val gapSeries: List<Float>? = null,
    val wattsSeries: List<Int>? = null,
    val weightedAveragePower: Float? = null,
    val maxPower: Float? = null,
    val hasPowerMeter: Boolean = false,
    val averageTemp: Int? = null,
    val elevHigh: Float? = null,
    val elevLow: Float? = null,
    val deviceName: String? = null,
    val gearName: String? = null,
    val gearDistance: Long? = null,
    val splits: List<ActivitySplit>? = null,
    val bestEfforts: List<ActivityBestEffort>? = null,
    val z1Seconds: Long = 0,
    val z2Seconds: Long = 0,
    val z3Seconds: Long = 0,
    val z4Seconds: Long = 0,
    val z5Seconds: Long = 0
) {
    val totalIntensitySeconds: Long get() = z1Seconds + z2Seconds + z3Seconds + z4Seconds + z5Seconds
}

data class ActivityBestEffort(
    val name: String,
    val movingTimeSeconds: Int,
    val distanceMeters: Float
)
