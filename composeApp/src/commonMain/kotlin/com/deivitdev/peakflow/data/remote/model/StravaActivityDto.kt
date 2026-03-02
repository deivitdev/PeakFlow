package com.deivitdev.peakflow.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StravaActivityDto(
    val id: Long,
    val name: String,
    val type: String? = null,
    @SerialName("sport_type") val sportType: String? = null,
    val distance: Float,
    @SerialName("moving_time") val movingTime: Int,
    @SerialName("elapsed_time") val elapsedTime: Int,
    @SerialName("total_elevation_gain") val totalElevationGain: Float,
    @SerialName("start_date") val startDate: String,
    @SerialName("average_speed") val averageSpeed: Float,
    @SerialName("max_speed") val maxSpeed: Float,
    @SerialName("average_heartrate") val averageHeartRate: Float? = null,
    @SerialName("max_heartrate") val maxHeartRate: Float? = null,
    val calories: Float? = null,
    val kilojoules: Float? = null,
    @SerialName("suffer_score") val sufferScore: Float? = null,
    @SerialName("average_watts") val averageWatts: Float? = null,
    @SerialName("weighted_average_watts") val weightedAverageWatts: Float? = null,
    @SerialName("max_watts") val maxWatts: Float? = null,
    @SerialName("device_watts") val deviceWatts: Boolean? = null,
    @SerialName("average_temp") val averageTemp: Int? = null,
    @SerialName("elev_high") val elevHigh: Float? = null,
    @SerialName("elev_low") val elevLow: Float? = null,
    @SerialName("device_name") val deviceName: String? = null,
    @SerialName("location_city") val locationCity: String? = null,
    @SerialName("location_country") val locationCountry: String? = null,
    val gear: StravaGearDto? = null,
    @SerialName("splits_metric") val splitsMetric: List<StravaSplitDto>? = null,
    val map: StravaMapDto? = null,
    @SerialName("best_efforts") val bestEfforts: List<StravaBestEffortDto>? = null
)

@Serializable
data class StravaBestEffortDto(
    val id: Long,
    val name: String,
    @SerialName("elapsed_time") val elapsedTime: Int,
    @SerialName("moving_time") val movingTime: Int,
    @SerialName("start_index") val startIndex: Int,
    @SerialName("end_index") val endIndex: Int,
    val distance: Float
)

@Serializable
data class StravaGearDto(
    val id: String,
    val name: String? = null,
    val distance: Long? = null
)

@Serializable
data class StravaSplitDto(
    val distance: Float,
    @SerialName("elapsed_time") val elapsedTime: Int,
    @SerialName("elevation_difference") val elevationDifference: Float,
    @SerialName("moving_time") val movingTime: Int,
    val split: Int,
    @SerialName("average_speed") val averageSpeed: Float,
    @SerialName("average_heartrate") val averageHeartRate: Float? = null,
    val pace_zone: Int? = null
)

@Serializable
data class StravaMapDto(
    val id: String,
    val polyline: String? = null,
    @SerialName("summary_polyline") val summaryPolyline: String? = null
)
