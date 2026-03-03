package com.deivitdev.peakflow.domain.model

data class FitnessFatiguePoint(
    val date: String,
    val fitness: Float, // CTL
    val fatigue: Float, // ATL
    val form: Float     // TSB
)

enum class TrainingStatus {
    FRESH,
    OPTIMAL,
    NEUTRAL,
    OVERREACHING,
    RECOVERY,
    BUILDING_DATA
}

data class FitnessFatigueData(
    val history: List<FitnessFatiguePoint>,
    val currentStatus: TrainingStatus,
    val currentFitness: Float,
    val currentFatigue: Float,
    val currentForm: Float,
    val projection: List<FitnessFatiguePoint>? = null,
    val rampRate: RampRateData? = null
)
