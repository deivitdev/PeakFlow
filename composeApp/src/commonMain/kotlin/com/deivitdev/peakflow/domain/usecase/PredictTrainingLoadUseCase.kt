package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.FitnessFatigueData
import com.deivitdev.peakflow.domain.model.FuelingStrategy
import com.deivitdev.peakflow.domain.model.PathOption
import com.deivitdev.peakflow.domain.model.TrainingPathType
import kotlin.math.roundToInt

class PredictTrainingLoadUseCase {
    operator fun invoke(ffData: FitnessFatigueData): List<PathOption> {
        val ctl = ffData.currentFitness
        val atl = ffData.currentFatigue
        val currentTsb = ctl - atl

        fun calculateTssForTargetTsb(targetTsb: Float): Int {
            val tss = (targetTsb - currentTsb + ctl / 42f - atl / 7f) * (-42f / 5f)
            return tss.roundToInt().coerceIn(0, 300)
        }

        fun getFuelingStrategy(tss: Int): FuelingStrategy {
            return when {
                tss > 150 -> FuelingStrategy.LOADING
                tss > 80 -> FuelingStrategy.PERFORMANCE
                tss > 30 -> FuelingStrategy.MAINTENANCE
                else -> FuelingStrategy.RECOVERY
            }
        }

        // 1. Build Path (Target TSB ~ -20)
        val buildTss = calculateTssForTargetTsb(-20f)
        val buildOption = PathOption(
            type = TrainingPathType.BUILD,
            tssTarget = buildTss,
            predictedTsb = -20,
            estimatedCarbGrams = (buildTss * 1.5f), // Rough estimation
            fuelingStrategy = getFuelingStrategy(buildTss)
        )

        // 2. Maintain Path (Target TSB ~ -5)
        val maintainTss = calculateTssForTargetTsb(-5f)
        val maintainOption = PathOption(
            type = TrainingPathType.MAINTAIN,
            tssTarget = maintainTss,
            predictedTsb = -5,
            estimatedCarbGrams = (maintainTss * 1.2f),
            fuelingStrategy = getFuelingStrategy(maintainTss)
        )

        // 3. Recover Path (TSS < 20)
        val recoverTss = 15
        val nextCtl = ctl + (recoverTss - ctl) / 42f
        val nextAtl = atl + (recoverTss - atl) / 7f
        val recoverOption = PathOption(
            type = TrainingPathType.RECOVER,
            tssTarget = recoverTss,
            predictedTsb = (nextCtl - nextAtl).roundToInt(),
            estimatedCarbGrams = (recoverTss * 0.8f),
            fuelingStrategy = FuelingStrategy.RECOVERY
        )

        return listOf(buildOption, maintainOption, recoverOption)
    }
}
