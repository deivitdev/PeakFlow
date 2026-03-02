package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.model.WorkoutType
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class AuditSportBalanceTest {
    private val useCase = AuditSportBalanceUseCase()

    @Test
    fun testStrengthAlert() {
        val now = Clock.System.now().toString()
        val activities = listOf(
            createActivity(now, WorkoutType.CYCLING.GENERIC),
            createActivity(now, WorkoutType.CYCLING.GENERIC),
            createActivity(now, WorkoutType.CYCLING.GENERIC)
        )

        val alert = useCase(activities)
        assertEquals(com.deivitdev.peakflow.domain.model.BalanceAlert.STRENGTH_NEGLECT, alert)
    }

    @Test
    fun testNoAlertWithStrength() {
        val now = Clock.System.now().toString()
        val activities = listOf(
            createActivity(now, WorkoutType.CYCLING.GENERIC),
            createActivity(now, WorkoutType.STRENGTH)
        )

        val alert = useCase(activities)
        assertNull(alert)
    }

    private fun createActivity(date: String, type: WorkoutType) = Activity(
        id = "1",
        name = "Test",
        type = type,
        distanceKm = 10f,
        movingTimeSeconds = 3600,
        elapsedTimeSeconds = 3600,
        totalElevationGainMeters = 0f,
        startDate = date,
        averageSpeedKmh = 10f,
        maxSpeedKmh = 10f
    )
    
    private fun assertTrue(condition: Boolean, message: String) {
        if (!condition) throw AssertionError(message)
    }
}
