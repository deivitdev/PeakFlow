package com.deivitdev.peakflow.domain.usecase

import com.deivitdev.peakflow.domain.model.UserProfile
import com.deivitdev.peakflow.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetUserProfileUseCase(private val repository: UserRepository) {
    operator fun invoke(): Flow<UserProfile?> = repository.getUserProfile()
}

class SaveUserProfileUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(profile: UserProfile) = repository.saveUserProfile(profile)
}

class SetLanguageUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(language: String?, currentProfile: UserProfile) {
        repository.saveUserProfile(currentProfile.copy(language = language))
    }
}

class GetLanguageUseCase(private val repository: UserRepository) {
    fun invoke(): Flow<String?> = repository.getUserProfile().map { it?.language }
}
