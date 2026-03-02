package com.deivitdev.peakflow.presentation.activity_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.usecase.CalculatePerformanceMetricsUseCase
import com.deivitdev.peakflow.domain.usecase.GetActivityDetailUseCase
import com.deivitdev.peakflow.domain.usecase.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

import com.deivitdev.peakflow.domain.model.HeartRateZone
import androidx.compose.ui.graphics.Color

data class ActivityDetailUiState(
    val activity: Activity? = null,
    val hrZones: List<HeartRateZone> = emptyList(),
    val userName: String? = null,
    val userPhotoUrl: String? = null,
    val ifValue: Float? = null,
    val tssValue: Float? = null,
    val decouplingPercentage: Float? = null,
    val aerobicStatus: com.deivitdev.peakflow.domain.model.AerobicStatus = com.deivitdev.peakflow.domain.model.AerobicStatus.INSUFFICIENT_DATA,
    val avgWkg: Float? = null,
    val maxWkg: Float? = null,
    val avgGapKmh: Float? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ActivityDetailViewModel(
    private val getActivityDetailUseCase: GetActivityDetailUseCase,
    private val calculatePerformanceMetricsUseCase: CalculatePerformanceMetricsUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActivityDetailUiState())
    val uiState = _uiState.asStateFlow()

    fun loadActivity(id: String) {
        viewModelScope.launch {
            _uiState.value = ActivityDetailUiState(isLoading = true)
            val activity = getActivityDetailUseCase(id)
            val userProfile = getUserProfileUseCase().firstOrNull() // Get latest user profile

            if (activity != null) {
                val zones = calculateHrZones(activity.heartRateSeries, activity.movingTimeSeconds)
                val performanceMetrics = calculatePerformanceMetricsUseCase(activity, userProfile)

                _uiState.value = _uiState.value.copy(
                    activity = activity, 
                    hrZones = zones,
                    userName = userProfile?.name,
                    userPhotoUrl = userProfile?.profilePhotoUrl,
                    ifValue = performanceMetrics.ifValue,
                    tssValue = performanceMetrics.tssValue,
                    decouplingPercentage = performanceMetrics.decouplingPercentage,
                    aerobicStatus = performanceMetrics.aerobicStatus,
                    avgWkg = performanceMetrics.avgWkg,
                    maxWkg = performanceMetrics.maxWkg,
                    avgGapKmh = performanceMetrics.avgGapKmh,
                    isLoading = false
                )
            } else {
                _uiState.value = _uiState.value.copy(error = "Activity not found", isLoading = false)
            }
        }
    }

    fun clearState() {
        _uiState.value = ActivityDetailUiState()
    }

    private fun calculateHrZones(heartRateSeries: List<Int>?, totalMovingTimeSeconds: Int): List<HeartRateZone> {
        if (heartRateSeries == null || heartRateSeries.isEmpty()) return emptyList()

        val maxHr = 190f // Default max HR
        val zones = MutableList(5) { 0 }

        heartRateSeries.forEach { bpm ->
            val percentage = (bpm / maxHr) * 100
            when {
                percentage < 60 -> zones[0]++
                percentage < 70 -> zones[1]++
                percentage < 80 -> zones[2]++
                percentage < 90 -> zones[3]++
                else -> zones[4]++
            }
        }

        val totalPoints = heartRateSeries.size.toFloat()
        val zoneColors = listOf(
            Color(0xFF9E9E9E), // Z1: Gray
            Color(0xFF2196F3), // Z2: Blue
            Color(0xFF4CAF50), // Z3: Green
            Color(0xFFFFC107), // Z4: Orange
            Color(0xFFF44336)  // Z5: Red
        )
        val zoneNames = listOf("Z1", "Z2", "Z3", "Z4", "Z5")

        return zones.mapIndexed { index, count ->
            val percentage = (count / totalPoints)
            HeartRateZone(
                id = index + 1,
                name = zoneNames[index],
                durationSeconds = (percentage * totalMovingTimeSeconds).toInt(), 
                percentage = percentage * 100,
                color = zoneColors[index]
            )
        }
    }
}
