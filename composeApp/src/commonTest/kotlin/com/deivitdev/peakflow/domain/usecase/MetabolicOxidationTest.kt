package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.model.WorkoutType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MetabolicOxidationTest {
    private val useCase = CalculateMetabolicOxidationUseCase()

    @Test
    fun testMetabolicOxidationZ1Z2() {
        val activity = Activity(
            id = "1",
            name = "Slow Ride",
            type = WorkoutType.CYCLING.GENERIC,
            distanceKm = 20f,
            movingTimeSeconds = 3600,
            elapsedTimeSeconds = 3600,
            totalElevationGainMeters = 100f,
            startDate = "2026-03-01T10:00:00Z",
            averageSpeedKmh = 20f,
            maxSpeedKmh = 30f,
            calories = 500f,
            z1Seconds = 1800,
            z2Seconds = 1800,
            z3Seconds = 0,
            z4Seconds = 0,
            z5Seconds = 0
        )

        val result = useCase(activity)

        // 70% Fat, 30% Carbs
        // Fat: (500 * 0.7) / 9 = 38.88
        // Carb: (500 * 0.3) / 4 = 37.5
        assertEquals(38.88f, result.fatGrams, 0.1f)
        assertEquals(37.5f, result.carbGrams, 0.1f)
        assertEquals(0, result.recoveryHours)
    }

    @Test
    fun testMetabolicOxidationHighIntensity() {
        val activity = Activity(
            id = "2",
            name = "Fast Ride",
            type = WorkoutType.CYCLING.GENERIC,
            distanceKm = 40f,
            movingTimeSeconds = 3600,
            elapsedTimeSeconds = 3600,
            totalElevationGainMeters = 100f,
            startDate = "2026-03-01T10:00:00Z",
            averageSpeedKmh = 40f,
            maxSpeedKmh = 50f,
            calories = 1000f,
            z1Seconds = 0,
            z2Seconds = 0,
            z3Seconds = 0,
            z4Seconds = 1800,
            z5Seconds = 1800
        )

        val result = useCase(activity)

        // Z4: 20% Fat, 80% Carbs
        // Z5: 5% Fat, 95% Carbs
        // Avg Fat Frac: (0.2 + 0.05) / 2 = 0.125
        // Avg Carb Frac: (0.8 + 0.95) / 2 = 0.875
        // Fat: (1000 * 0.125) / 9 = 13.88
        // Carb: (1000 * 0.875) / 4 = 218.75
        assertEquals(13.88f, result.fatGrams, 0.1f)
        assertEquals(218.75f, result.carbGrams, 0.1f)
        assertTrue(result.recoveryHours >= 24)
    }
}
