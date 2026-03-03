package com.deivitdev.peakflow.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.deivitdev.peakflow.domain.model.UserProfile
import com.deivitdev.peakflow.domain.repository.ActivityRepository
import com.deivitdev.peakflow.domain.usecase.GetUserProfileUseCase
import com.deivitdev.peakflow.domain.usecase.SaveUserProfileUseCase
import com.deivitdev.peakflow.Platform
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProfileUiState(
    val name: String = "",
    val weight: String = "",
    val height: String = "",
    val isDarkMode: Boolean? = null,
    val language: String? = null,
    val profilePhotoUrl: String? = null,
    val ftpWatts: String = "",
    val weeklyGoalHours: String = "5",
    val isConnectedToStrava: Boolean = false,
    val isLoading: Boolean = false,
    val relativePower: com.deivitdev.peakflow.domain.usecase.RelativePowerResult? = null
)

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val saveUserProfileUseCase: SaveUserProfileUseCase,
    private val activityRepository: ActivityRepository,
    private val calculateRelativePowerUseCase: com.deivitdev.peakflow.domain.usecase.CalculateRelativePowerUseCase,
    private val platform: Platform
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getUserProfileUseCase().collect { profile ->
                profile?.let {
                    _uiState.update { state ->
                        val relPower = it.ftpWatts?.let { ftp ->
                            calculateRelativePowerUseCase(ftp.toFloat(), it.weightKg)
                        }
                        state.copy(
                            name = it.name,
                            weight = it.weightKg.toString(),
                            height = it.heightCm.toString(),
                            isDarkMode = it.isDarkMode,
                            language = it.language,
                            profilePhotoUrl = it.profilePhotoUrl,
                            ftpWatts = it.ftpWatts?.toString() ?: "",
                            weeklyGoalHours = it.weeklyGoalHours.toString(),
                            relativePower = relPower
                        )
                    }
                }
            }
        }
        
        viewModelScope.launch {
            activityRepository.connectionStatusFlow().collect { connected ->
                _uiState.update { it.copy(isConnectedToStrava = connected) }
            }
        }
    }

    fun onNameChange(newName: String) = _uiState.update { it.copy(name = newName) }
    fun onWeightChange(newWeight: String) = _uiState.update { it.copy(weight = newWeight) }
    fun onHeightChange(newHeight: String) = _uiState.update { it.copy(height = newHeight) }
    fun onFtpWattsChange(newFtpWatts: String) = _uiState.update { it.copy(ftpWatts = newFtpWatts) }
    fun onWeeklyGoalChange(newGoal: String) = _uiState.update { it.copy(weeklyGoalHours = newGoal) }
    
    fun onLanguageChange(lang: String?) {
        _uiState.update { it.copy(language = lang) }
        platform.setLanguage(lang)
        saveProfile()
    }

    fun onThemeChange(isDark: Boolean?) {
        _uiState.update { it.copy(isDarkMode = isDark) }
        saveProfile() // Persist immediately
    }

    fun saveProfile() {
        val state = _uiState.value
        viewModelScope.launch {
            saveUserProfileUseCase(
                UserProfile(
                    name = state.name,
                    weightKg = state.weight.toFloatOrNull() ?: 0f,
                    heightCm = state.height.toFloatOrNull() ?: 0f,
                    isDarkMode = state.isDarkMode,
                    language = state.language,
                    profilePhotoUrl = state.profilePhotoUrl,
                    ftpWatts = state.ftpWatts.toIntOrNull(),
                    weeklyGoalHours = state.weeklyGoalHours.toFloatOrNull() ?: 5f
                )
            )
        }
    }

    fun syncStravaProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            activityRepository.getAuthenticatedAthlete().onSuccess { athlete ->
                _uiState.update { state ->
                    state.copy(
                        name = "${athlete.firstname ?: ""} ${athlete.lastname ?: ""}".trim(),
                        weight = (athlete.weight ?: state.weight.toFloatOrNull() ?: 0f).toString(),
                        ftpWatts = (athlete.ftp ?: state.ftpWatts.toIntOrNull() ?: 0).toString(),
                        profilePhotoUrl = athlete.profile ?: athlete.profileMedium,
                        isLoading = false
                    )
                }
                saveProfile()
            }.onFailure {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun disconnectStrava() {
        viewModelScope.launch {
            activityRepository.disconnect()
            _uiState.update { it.copy(isConnectedToStrava = false) }
        }
    }
}
