package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.model.MetabolicSummary
import kotlin.math.roundToInt

class CalculateMetabolicOxidationUseCase {
    operator fun invoke(activity: Activity): MetabolicSummary {
        val calories = activity.calories ?: 0f
        val totalSeconds = (activity.z1Seconds + activity.z2Seconds + activity.z3Seconds + activity.z4Seconds + activity.z5Seconds).toFloat()
        
        if (totalSeconds <= 0f || calories <= 0f) {
            return MetabolicSummary(0f, 0f, 0)
        }

        val z12Frac = (activity.z1Seconds + activity.z2Seconds) / totalSeconds
        val z3Frac = activity.z3Seconds / totalSeconds
        val z4Frac = activity.z4Seconds / totalSeconds
        val z5Frac = activity.z5Seconds / totalSeconds

        val fatFraction = (z12Frac * 0.7f) + (z3Frac * 0.5f) + (z4Frac * 0.2f) + (z5Frac * 0.05f)
        val carbFraction = (z12Frac * 0.3f) + (z3Frac * 0.5f) + (z4Frac * 0.8f) + (z5Frac * 0.95f)

        val fatGrams = (calories * fatFraction) / 9f
        val carbGrams = (calories * carbFraction) / 4f

        // Hydration logic
        // Use averagePower / ftp if weightedAveragePower is not available for IF
        // But the Activity model already has IF-like metrics in other usecases.
        // For simplicity, we'll check if IF is high (using a proxy or if we can calculate it)
        // Let's assume we can get Intensity Factor if we have weightedAveragePower and ftp.
        // Since we don't have FTP here directly (it's in UserProfile), we might need it.
        // But the spec says: "Intensity Factor (IF) is above 0.85"
        
        // Recovery logic
        val recoveryHours = if (carbGrams > 280f) 48 else if (carbGrams > 150f) 24 else 0

        return MetabolicSummary(
            carbGrams = carbGrams,
            fatGrams = fatGrams,
            recoveryHours = recoveryHours
        )
    }
}
