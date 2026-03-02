package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.AggregatedZones
import com.deivitdev.peakflow.domain.model.PolarizationSummary

class CalculatePolarizationRatioUseCase {
    operator fun invoke(aggregatedZones: AggregatedZones): PolarizationSummary {
        val low = (aggregatedZones.z1Seconds + aggregatedZones.z2Seconds).toFloat()
        val threshold = aggregatedZones.z3Seconds.toFloat()
        val high = (aggregatedZones.z4Seconds + aggregatedZones.z5Seconds).toFloat()
        val total = low + threshold + high

        if (total <= 0f) {
            return PolarizationSummary(0f, 0f, 0f, false)
        }

        val lowP = (low / total) * 100f
        val thresholdP = (threshold / total) * 100f
        val highP = (high / total) * 100f

        return PolarizationSummary(
            lowIntensityPercentage = lowP,
            thresholdIntensityPercentage = thresholdP,
            highIntensityPercentage = highP,
            isGreyZoneAlert = thresholdP > 40f
        )
    }
}
