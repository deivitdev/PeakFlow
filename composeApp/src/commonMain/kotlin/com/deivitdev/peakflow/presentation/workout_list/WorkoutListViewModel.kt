package com.deivitdev.peakflow.presentation.workout_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.repository.ActivityRepository
import com.deivitdev.peakflow.domain.usecase.SyncActivitiesUseCase
import com.deivitdev.peakflow.domain.model.WorkoutType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WorkoutListUiState(
    val activities: List<Activity> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: WorkoutType? = null,
    val error: String? = null
)

class WorkoutListViewModel(
    private val repository: ActivityRepository,
    private val syncActivitiesUseCase: SyncActivitiesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutListUiState())
    val uiState = _uiState.asStateFlow()

    private var allActivities: List<Activity> = emptyList()

    init {
        viewModelScope.launch {
            repository.getActivitiesFlow().collect { activities ->
                allActivities = activities
                applyFilters()
            }
        }
    }

    fun loadActivities() {
        // This is now handled reactively by the flow in init
        // But we keep it for manual triggers if needed, though it's redundant now
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            allActivities = repository.getActivities()
            applyFilters()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onCategorySelect(category: WorkoutType?) {
        _uiState.update { it.copy(selectedCategory = if (_uiState.value.selectedCategory == category) null else category) }
        applyFilters()
    }

    private fun applyFilters() {
        val query = _uiState.value.searchQuery.lowercase()
        val category = _uiState.value.selectedCategory

        val filtered = allActivities.filter { activity ->
            val matchesQuery = activity.name.lowercase().contains(query) || 
                             activity.location?.lowercase()?.contains(query) == true
            
            val matchesCategory = when {
                category == null -> true
                category is WorkoutType.CYCLING -> activity.type is WorkoutType.CYCLING
                category is WorkoutType.RUNNING -> activity.type is WorkoutType.RUNNING
                category == WorkoutType.STRENGTH -> activity.type == WorkoutType.STRENGTH
                category == WorkoutType.WALKING -> activity.type == WorkoutType.WALKING
                else -> activity.type == category
            }

            matchesQuery && matchesCategory
        }

        _uiState.update { it.copy(activities = filtered) }
    }

    fun refreshActivities() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            val result = syncActivitiesUseCase()
            result.onSuccess {
                loadActivities()
            }.onFailure { throwable ->
                _uiState.update { it.copy(error = throwable.message) }
            }
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }
}
