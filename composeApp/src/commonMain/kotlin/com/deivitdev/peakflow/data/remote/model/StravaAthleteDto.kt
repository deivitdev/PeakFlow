package com.deivitdev.peakflow.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StravaAthleteDto(
    val id: Long,
    val firstname: String? = null,
    val lastname: String? = null,
    @SerialName("profile_medium") val profileMedium: String? = null,
    val profile: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val weight: Float? = null,
    val ftp: Int? = null
)
