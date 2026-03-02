package com.deivitdev.peakflow.data.remote

import com.deivitdev.peakflow.data.remote.model.StravaActivityDto
import com.deivitdev.peakflow.data.remote.model.StravaAthleteDto
import com.deivitdev.peakflow.data.remote.model.StravaStreamDto
import com.deivitdev.peakflow.data.remote.model.StravaTokenDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class StravaApiClient(private val httpClient: HttpClient) {

    fun getAuthorizationUrl(clientId: String, redirectUri: String): String {
        return URLBuilder("https://www.strava.com/oauth/mobile/authorize").apply {
            parameters.append("client_id", clientId)
            parameters.append("redirect_uri", redirectUri)
            parameters.append("response_type", "code")
            parameters.append("approval_prompt", "auto")
            parameters.append("scope", "activity:read_all")
        }.buildString()
    }

    suspend fun getAccessToken(code: String, clientId: String, clientSecret: String): StravaTokenDto {
        return httpClient.post("https://www.strava.com/oauth/token") {
            setBody(FormDataContent(Parameters.build {
                append("client_id", clientId)
                append("client_secret", clientSecret)
                append("code", code)
                append("grant_type", "authorization_code")
            }))
        }.body()
    }

    suspend fun refreshAccessToken(refreshToken: String, clientId: String, clientSecret: String): StravaTokenDto {
        return httpClient.post("https://www.strava.com/oauth/token") {
            setBody(FormDataContent(Parameters.build {
                append("client_id", clientId)
                append("client_secret", clientSecret)
                append("refresh_token", refreshToken)
                append("grant_type", "refresh_token")
            }))
        }.body()
    }

    suspend fun getActivities(accessToken: String, page: Int = 1, perPage: Int = 30): List<StravaActivityDto> {
        return httpClient.get("https://www.strava.com/api/v3/athlete/activities") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("page", page)
            parameter("per_page", perPage)
        }.body()
    }

    suspend fun getAuthenticatedAthlete(accessToken: String): StravaAthleteDto {
        return httpClient.get("https://www.strava.com/api/v3/athlete") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    suspend fun getActivityDetail(accessToken: String, activityId: String): StravaActivityDto {
        return httpClient.get("https://www.strava.com/api/v3/activities/$activityId") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }.body()
    }

    suspend fun getStreams(accessToken: String, activityId: String): Map<String, StravaStreamDto> {
        return httpClient.get("https://www.strava.com/api/v3/activities/$activityId/streams") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("keys", "heartrate,altitude,distance,time,cadence,velocity_smooth,watts,grade_adjusted_pace")
            parameter("key_by_type", true)
        }.body()
    }

    companion object {
        fun createDefaultHttpClient() = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
        }
    }
}
