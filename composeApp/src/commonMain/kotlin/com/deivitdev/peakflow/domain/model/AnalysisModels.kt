package com.deivitdev.peakflow.domain.model

data class AggregatedZones(
    val z1Seconds: Long,
    val z2Seconds: Long,
    val z3Seconds: Long,
    val z4Seconds: Long,
    val z5Seconds: Long
)

data class MetabolicSummary(
    val carbGrams: Float,
    val fatGrams: Float,
    val recoveryHours: Int
)

data class PolarizationSummary(
    val lowIntensityPercentage: Float,
    val thresholdIntensityPercentage: Float,
    val highIntensityPercentage: Float,
    val isGreyZoneAlert: Boolean
)

enum class TrainingPathType {
    BUILD, MAINTAIN, RECOVER
}

enum class BalanceAlert {
    STRENGTH_NEGLECT, HIGH_FREQUENCY
}

enum class FuelingStrategy {
    LOADING, PERFORMANCE, MAINTENANCE, RECOVERY
}

enum class InsightType {
    HYDRATION, GREY_ZONE, FUELING, BALANCE_ALERT
}

enum class RampRateStatus {
    MAINTENANCE, PRODUCTIVE, INTENSE, RISKY
}

data class RampRateData(
    val weeklyIncrease: Float,
    val status: RampRateStatus
)

data class CoachInsight(
    val type: InsightType,
    val title: String,
    val message: String,
    val value: String? = null,
    val strategy: FuelingStrategy? = null
)

data class PathOption(
    val type: TrainingPathType,
    val tssTarget: Int,
    val predictedTsb: Int,
    val recommendedSport: WorkoutType? = null,
    val estimatedCarbGrams: Float = 0f,
    val fuelingStrategy: FuelingStrategy = FuelingStrategy.MAINTENANCE,
    val estimatedCyclingDuration: String? = null,
    val estimatedRunningDuration: String? = null
)

data class TrainingRecommendation(
    val options: List<PathOption>,
    val priorityAlert: BalanceAlert? = null
)
