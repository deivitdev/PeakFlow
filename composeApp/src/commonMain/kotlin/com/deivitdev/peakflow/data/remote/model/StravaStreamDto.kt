package com.deivitdev.peakflow.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class StravaStreamDto(
    val type: String? = null,
    val data: List<JsonElement>, // Can be Float or Int depending on type
    @SerialName("series_type") val seriesType: String,
    @SerialName("original_size") val originalSize: Int,
    val resolution: String
)
