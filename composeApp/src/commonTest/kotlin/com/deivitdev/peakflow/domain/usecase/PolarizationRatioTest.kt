package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.AggregatedZones
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PolarizationRatioTest {
    private val useCase = CalculatePolarizationRatioUseCase()

    @Test
    fun testPolarizedDistribution() {
        val zones = AggregatedZones(
            z1Seconds = 8000,
            z2Seconds = 0,
            z3Seconds = 0,
            z4Seconds = 2000,
            z5Seconds = 0
        )

        val result = useCase(zones)

        assertEquals(80f, result.lowIntensityPercentage)
        assertEquals(0f, result.thresholdIntensityPercentage)
        assertEquals(20f, result.highIntensityPercentage)
        assertEquals(false, result.isGreyZoneAlert)
    }

    @Test
    fun testGreyZoneAlert() {
        val zones = AggregatedZones(
            z1Seconds = 2000,
            z2Seconds = 0,
            z3Seconds = 6000,
            z4Seconds = 2000,
            z5Seconds = 0
        )

        val result = useCase(zones)

        println("Threshold %: ${result.thresholdIntensityPercentage}")
        assertEquals(60f, result.thresholdIntensityPercentage, 0.1f)
        assertTrue(result.isGreyZoneAlert)
    }
}
