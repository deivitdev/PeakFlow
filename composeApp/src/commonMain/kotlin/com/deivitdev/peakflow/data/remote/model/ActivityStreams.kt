package com.deivitdev.peakflow.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class ActivityStreams(
    val heartRate: List<Int>? = null,
    val elevation: List<Float>? = null,
    val cadence: List<Int>? = null,
    val velocitySmooth: List<Float>? = null,
    val watts: List<Int>? = null,
    val gradeAdjustedPace: List<Float>? = null
)
