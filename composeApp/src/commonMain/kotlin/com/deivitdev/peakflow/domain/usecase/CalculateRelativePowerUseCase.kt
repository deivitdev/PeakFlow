package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.UserProfile

enum class PerformanceCategory {
    RECREATIONAL,
    INTERMEDIATE,
    ADVANCED,
    ELITE,
    WORLD_CLASS,
    UNKNOWN
}

data class RelativePowerResult(
    val wKg: Float,
    val category: PerformanceCategory,
    val nextCategory: PerformanceCategory?,
    val wattsToNextLevel: Float?,
    val weightToNextLevel: Float?
)

class CalculateRelativePowerUseCase {
    operator fun invoke(watts: Float, weightKg: Float): RelativePowerResult {
        if (weightKg <= 0f) return RelativePowerResult(0f, PerformanceCategory.UNKNOWN, null, null, null)
        
        val wKg = watts / weightKg
        val category = when {
            wKg < 2.0f -> PerformanceCategory.RECREATIONAL
            wKg < 3.0f -> PerformanceCategory.INTERMEDIATE
            wKg < 4.0f -> PerformanceCategory.ADVANCED
            wKg < 5.0f -> PerformanceCategory.ELITE
            else -> PerformanceCategory.WORLD_CLASS
        }

        val nextCategory = when (category) {
            PerformanceCategory.RECREATIONAL -> PerformanceCategory.INTERMEDIATE
            PerformanceCategory.INTERMEDIATE -> PerformanceCategory.ADVANCED
            PerformanceCategory.ADVANCED -> PerformanceCategory.ELITE
            PerformanceCategory.ELITE -> PerformanceCategory.WORLD_CLASS
            else -> null
        }

        val targetWkg = when (nextCategory) {
            PerformanceCategory.INTERMEDIATE -> 2.0f
            PerformanceCategory.ADVANCED -> 3.0f
            PerformanceCategory.ELITE -> 4.0f
            PerformanceCategory.WORLD_CLASS -> 5.0f
            else -> null
        }

        val wattsToNextLevel = targetWkg?.let { (it * weightKg) - watts }
        val weightToNextLevel = targetWkg?.let { weightKg - (watts / it) }

        return RelativePowerResult(
            wKg = wKg,
            category = category,
            nextCategory = nextCategory,
            wattsToNextLevel = wattsToNextLevel,
            weightToNextLevel = weightToNextLevel
        )
    }
}
