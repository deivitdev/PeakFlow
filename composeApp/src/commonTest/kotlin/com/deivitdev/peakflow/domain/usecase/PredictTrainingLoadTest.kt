package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.FitnessFatigueData
import com.deivitdev.peakflow.domain.model.TrainingPathType
import com.deivitdev.peakflow.domain.model.TrainingStatus
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PredictTrainingLoadTest {
    private val useCase = PredictTrainingLoadUseCase()

    @Test
    fun testPredictionMath() {
        val ffData = FitnessFatigueData(
            history = emptyList(),
            currentStatus = TrainingStatus.NEUTRAL,
            currentFitness = 50f, // CTL
            currentFatigue = 50f, // ATL
            currentForm = 0f      // TSB
        )

        val results = useCase(ffData)
        
        // Build Path should target TSB -20
        val build = results.find { it.type == TrainingPathType.BUILD }!!
        assertEquals(-20, build.predictedTsb)
        // Manual check:
        // tss = (-20 - 0 + 50/42 - 50/7) * (-42/5)
        // tss = (-20 + 1.19 - 7.14) * (-8.4)
        // tss = (-25.95) * (-8.4) = 217.98
        assertTrue(build.tssTarget > 200)

        // Maintain Path should target TSB -5
        val maintain = results.find { it.type == TrainingPathType.MAINTAIN }!!
        assertEquals(-5, maintain.predictedTsb)
        
        // Recover Path should have low TSS
        val recover = results.find { it.type == TrainingPathType.RECOVER }!!
        assertEquals(15, recover.tssTarget)
    }
}
