package com.deivitdev.peakflow.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.deivitdev.peakflow.db.PeakFlowDatabase
import com.deivitdev.peakflow.domain.model.UserProfile
import com.deivitdev.peakflow.domain.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

import kotlinx.coroutines.flow.distinctUntilChanged

class UserRepositoryImpl(database: PeakFlowDatabase) : UserRepository {
    private val queries = database.peakFlowDatabaseQueries

    override fun getUserProfile(): Flow<UserProfile?> {
        return queries.getUserProfile()
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .distinctUntilChanged()
            .map { profile ->
                profile?.let {
                    UserProfile(
                        name = it.name,
                        weightKg = it.weight.toFloat(),
                        heightCm = it.height.toFloat(),
                        isDarkMode = when(it.isDarkMode) {
                            0L -> false
                            1L -> true
                            else -> null
                        },
                        language = it.language,
                        profilePhotoUrl = it.profilePhotoUrl,
                        ftpWatts = it.ftpWatts?.toInt(),
                        hrMaxBpm = it.hrMax?.toInt(),
                        weeklyGoalHours = it.weeklyGoalHours?.toFloat() ?: 5f
                    )
                }
            }
    }

    override suspend fun saveUserProfile(profile: UserProfile) = withContext(Dispatchers.IO) {
        queries.upsertUserProfile(
            name = profile.name,
            weight = profile.weightKg.toDouble(),
            height = profile.heightCm.toDouble(),
            isDarkMode = when(profile.isDarkMode) {
                false -> 0L
                true -> 1L
                null -> null
            },
            language = profile.language,
            profilePhotoUrl = profile.profilePhotoUrl,
            ftpWatts = profile.ftpWatts?.toLong(),
            hrMax = profile.hrMaxBpm?.toLong(),
            weeklyGoalHours = profile.weeklyGoalHours.toDouble()
        )
    }
}
