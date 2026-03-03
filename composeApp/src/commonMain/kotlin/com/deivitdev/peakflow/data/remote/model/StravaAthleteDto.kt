package com.deivitdev.peakflow.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StravaAthleteDto(
    val id: Long,
    val username: String? = null,
    val firstname: String? = null,
    val lastname: String? = null,
    val city: String? = null,
    val state: String? = null,
    val country: String? = null,
    val sex: String? = null,
    val premium: Boolean? = null,
    @SerialName("created_at") val createdAt: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("badge_type_id") val badgeTypeId: Int? = null,
    @SerialName("profile_medium") val profileMedium: String? = null,
    val profile: String? = null,
    val friend: String? = null,
    val follower: String? = null,
    val weight: Float? = null,
    val ftp: Int? = null
)
