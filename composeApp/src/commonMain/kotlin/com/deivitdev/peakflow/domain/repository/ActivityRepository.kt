package com.deivitdev.peakflow.domain.repository

import com.deivitdev.peakflow.data.remote.model.StravaAthleteDto
import com.deivitdev.peakflow.domain.model.Activity

interface ActivityRepository {
    suspend fun syncActivities(): Result<Unit>
    suspend fun getActivities(): List<Activity>
    fun getActivitiesFlow(): kotlinx.coroutines.flow.Flow<List<Activity>>
    suspend fun getActivity(id: String): Activity?
    suspend fun connect(code: String): Result<Unit>
    suspend fun disconnect(): Result<Unit>
    suspend fun isConnected(): Boolean
    suspend fun getAuthenticatedAthlete(): Result<StravaAthleteDto>
    suspend fun getAggregatedZones(startDate: String, endDate: String): com.deivitdev.peakflow.domain.model.AggregatedZones
    
    // Daily Load
    suspend fun getDailyLoads(startDate: String, endDate: String): List<com.deivitdev.peakflow.domain.model.DailyLoad>
    fun getDailyLoadsFlow(startDate: String, endDate: String): kotlinx.coroutines.flow.Flow<List<com.deivitdev.peakflow.domain.model.DailyLoad>>
    suspend fun saveDailyLoad(date: String, tss: Float)
    suspend fun clearDailyLoads()
}
