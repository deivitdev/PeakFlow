package com.deivitdev.peakflow.data.remote

import com.deivitdev.peakflow.data.remote.model.StravaActivityDto
import com.deivitdev.peakflow.data.remote.model.StravaAthleteDto
import com.deivitdev.peakflow.data.remote.model.StravaStreamDto
import com.deivitdev.peakflow.data.remote.model.StravaTokenDto
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class StravaRateLimitException(message: String) : Exception(message)

class StravaApiClient(private val httpClient: HttpClient) {

    private suspend fun HttpResponse.ensureSuccess() {
        if (!status.isSuccess()) {
            val body = bodyAsText()
            if (status == HttpStatusCode.TooManyRequests) {
                throw StravaRateLimitException("Strava rate limit exceeded. Please try again in 15 minutes.")
            }
            throw Exception("Strava API error (${status.value}): $body")
        }
    }

    fun getAuthorizationUrl(clientId: String, redirectUri: String): String {
        return URLBuilder("https://www.strava.com/oauth/mobile/authorize").apply {
            parameters.append("client_id", clientId)
            parameters.append("redirect_uri", redirectUri)
            parameters.append("response_type", "code")
            parameters.append("approval_prompt", "auto")
            parameters.append("scope", "read,activity:read_all")
        }.buildString()
    }

    suspend fun getAccessToken(code: String, clientId: String, clientSecret: String, redirectUri: String): StravaTokenDto {
        val response = httpClient.post("https://www.strava.com/oauth/token") {
            setBody(FormDataContent(Parameters.build {
                append("client_id", clientId)
                append("client_secret", clientSecret)
                append("code", code)
                append("grant_type", "authorization_code")
                append("redirect_uri", redirectUri)
            }))
        }
        response.ensureSuccess()
        return response.body()
    }

    suspend fun refreshAccessToken(refreshToken: String, clientId: String, clientSecret: String): StravaTokenDto {
        val response = httpClient.post("https://www.strava.com/oauth/token") {
            setBody(FormDataContent(Parameters.build {
                append("client_id", clientId)
                append("client_secret", clientSecret)
                append("refresh_token", refreshToken)
                append("grant_type", "refresh_token")
            }))
        }
        response.ensureSuccess()
        return response.body()
    }

    suspend fun getActivities(
        accessToken: String, 
        page: Int = 1, 
        perPage: Int = 30,
        after: Long? = null,
        before: Long? = null
    ): List<StravaActivityDto> {
        println("STRAVA_API: Fetching activities page=$page, perPage=$perPage, after=$after")
        val response = httpClient.get("https://www.strava.com/api/v3/athlete/activities") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("page", page)
            parameter("per_page", perPage)
            if (after != null) parameter("after", after)
            if (before != null) parameter("before", before)
        }
        response.ensureSuccess()
        val activities: List<StravaActivityDto> = response.body()
        println("STRAVA_API: Received ${activities.size} activities from API")
        return activities
    }

    suspend fun getAuthenticatedAthlete(accessToken: String): StravaAthleteDto {
        val response = httpClient.get("https://www.strava.com/api/v3/athlete") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }
        response.ensureSuccess()
        return response.body()
    }

    suspend fun getActivityDetail(accessToken: String, activityId: String): StravaActivityDto {
        val response = httpClient.get("https://www.strava.com/api/v3/activities/$activityId") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
        }
        response.ensureSuccess()
        return response.body()
    }

    suspend fun getStreams(accessToken: String, activityId: String): Map<String, StravaStreamDto> {
        val response = httpClient.get("https://www.strava.com/api/v3/activities/$activityId/streams") {
            header(HttpHeaders.Authorization, "Bearer $accessToken")
            parameter("keys", "heartrate,altitude,distance,time,cadence,velocity_smooth,watts,grade_adjusted_pace")
            parameter("key_by_type", true)
        }
        response.ensureSuccess()
        return response.body()
    }

    companion object {
        fun createDefaultHttpClient() = HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                    isLenient = true
                })
            }
            install(Logging) {
                level = LogLevel.INFO
                logger = object : Logger {
                    override fun log(message: String) {
                        println("KTOR_HTTP: $message")
                    }
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 15000
                connectTimeoutMillis = 15000
            }
        }
    }
}
