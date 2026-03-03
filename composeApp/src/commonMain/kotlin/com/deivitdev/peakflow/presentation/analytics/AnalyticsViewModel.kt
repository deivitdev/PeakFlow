package com.deivitdev.peakflow.presentation.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deivitdev.peakflow.domain.model.FitnessFatigueData
import com.deivitdev.peakflow.domain.model.PerformanceMetrics
import com.deivitdev.peakflow.domain.model.TrainingRecommendation
import com.deivitdev.peakflow.domain.usecase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job

data class AnalyticsUiState(
    val metrics: PerformanceMetrics? = null,
    val trends: List<TrendDataPoint> = emptyList(),
    val fitnessFatigue: FitnessFatigueData? = null,
    val relativePower: RelativePowerResult? = null,
    val userName: String? = null,
    val userPhotoUrl: String? = null,
    val advancedInsights: AdvancedPerformanceInsights? = null,
    val trainingRecommendation: TrainingRecommendation? = null,
    val selectedPathIndex: Int = 1, // Default to Maintain
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isRefreshingTrends: Boolean = false,
    val selectedGranularity: TrendGranularity = TrendGranularity.WEEKLY,
    val selectedMetric: TrendMetric = TrendMetric.DISTANCE
)

@OptIn(kotlinx.coroutines.FlowPreview::class)
class AnalyticsViewModel(
    private val getPerformanceMetricsUseCase: GetPerformanceMetricsUseCase,
    private val getTrendsUseCase: GetTrendsUseCase,
    private val getFitnessFatigueUseCase: GetFitnessFatigueUseCase,
    private val syncDailyLoadUseCase: SyncDailyLoadUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val calculateRelativePowerUseCase: CalculateRelativePowerUseCase,
    private val getAdvancedPerformanceInsightsUseCase: GetAdvancedPerformanceInsightsUseCase,
    private val getPredictiveCoachingUseCase: GetPredictiveCoachingUseCase,
    private val observeActivitiesUseCase: ObserveActivitiesUseCase,
    private val syncActivitiesUseCase: SyncActivitiesUseCase,
    private val hydrateRecentActivitiesUseCase: HydrateRecentActivitiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState = _uiState.asStateFlow()
    
    private var loadDataJob: Job? = null
    private var isCurrentlyLoading = false

    init {
        // Observe profile changes reactively
        viewModelScope.launch {
            getUserProfileUseCase().collect { profile ->
                profile?.let { userProfile ->
                    val relPower = userProfile.ftpWatts?.let { ftp ->
                        calculateRelativePowerUseCase(ftp.toFloat(), userProfile.weightKg)
                    }
                    _uiState.update { it.copy(
                        relativePower = relPower,
                        userName = userProfile.name,
                        userPhotoUrl = userProfile.profilePhotoUrl
                    ) }
                }
            }
        }

        // Reactive Observation: Reload data whenever activities change
        viewModelScope.launch {
            observeActivitiesUseCase()
                .debounce(2000)
                .collect {
                    loadData(showLoading = false)
                }
        }
    }

    fun loadData(showLoading: Boolean = true) {
        if (isCurrentlyLoading) return
        
        loadDataJob?.cancel()
        loadDataJob = viewModelScope.launch {
            isCurrentlyLoading = true
            try {
                if (showLoading) {
                    _uiState.update { it.copy(isLoading = true) }
                    // Hydrate details once at the start of a full load
                    hydrateRecentActivitiesUseCase()
                }
                
                // 1. Sync daily loads first
                syncDailyLoadUseCase()
                
                // 2. Fetch components with current granularity filter
                val dateRange = when (_uiState.value.selectedGranularity) {
                    TrendGranularity.WEEKLY -> 7
                    TrendGranularity.MONTHLY -> 30
                }
                
                val metrics = getPerformanceMetricsUseCase(dateRange)
                val trends = getTrendsUseCase(
                    metric = _uiState.value.selectedMetric,
                    granularity = _uiState.value.selectedGranularity
                )
                
                val initialFfData = getFitnessFatigueUseCase()
                val recommendations = initialFfData?.let { getPredictiveCoachingUseCase(it) }
                
                // Get projection for the default selected path (index 1: Maintain)
                val selectedPath = recommendations?.options?.getOrNull(_uiState.value.selectedPathIndex)
                val ffDataWithProj = getFitnessFatigueUseCase(futureTss = selectedPath?.tssTarget)
                
                val advancedInsights = getAdvancedPerformanceInsightsUseCase()

                _uiState.update { it.copy(
                    metrics = metrics, 
                    trends = trends, 
                    fitnessFatigue = ffDataWithProj,
                    advancedInsights = advancedInsights,
                    trainingRecommendation = recommendations,
                    isLoading = false 
                ) }
            } catch (e: com.deivitdev.peakflow.data.remote.StravaRateLimitException) {
                println("RATE_LIMIT_ERROR: ${e.message}")
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                println("LOAD_DATA_ERROR: ${e.message}")
                _uiState.update { it.copy(isLoading = false) }
            } finally {
                isCurrentlyLoading = false
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            syncActivitiesUseCase().onSuccess {
                loadData(showLoading = true) // Full load after sync
            }.onFailure {
                // Handle error if needed
            }
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }

    fun onPathSelected(index: Int) {
        _uiState.update { it.copy(selectedPathIndex = index) }
        
        viewModelScope.launch {
            val selectedPath = _uiState.value.trainingRecommendation?.options?.getOrNull(index)
            val ffData = getFitnessFatigueUseCase(futureTss = selectedPath?.tssTarget)
            _uiState.update { it.copy(fitnessFatigue = ffData) }
        }
    }

    fun loadTrends() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshingTrends = true) }
            
            val dateRange = when (_uiState.value.selectedGranularity) {
                TrendGranularity.WEEKLY -> 7
                TrendGranularity.MONTHLY -> 30
            }
            
            val metrics = getPerformanceMetricsUseCase(dateRange)
            val trends = getTrendsUseCase(
                metric = _uiState.value.selectedMetric,
                granularity = _uiState.value.selectedGranularity
            )
            _uiState.update { it.copy(
                metrics = metrics,
                trends = trends, 
                isRefreshingTrends = false
            ) }
        }
    }

    fun onGranularityChange(granularity: TrendGranularity) {
        _uiState.update { it.copy(selectedGranularity = granularity) }
        loadTrends()
    }

    fun onMetricChange(metric: TrendMetric) {
        _uiState.update { it.copy(selectedMetric = metric) }
        loadTrends()
    }
}
