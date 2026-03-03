package com.deivitdev.peakflow.data.mapper

import com.deivitdev.peakflow.data.remote.model.ActivityStreams
import com.deivitdev.peakflow.data.remote.model.StravaActivityDto
import com.deivitdev.peakflow.data.remote.model.StravaSplitDto
import com.deivitdev.peakflow.db.Activity as ActivityDb
import com.deivitdev.peakflow.domain.model.Activity
import com.deivitdev.peakflow.domain.model.ActivitySplit
import com.deivitdev.peakflow.domain.model.WorkoutType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

import com.deivitdev.peakflow.data.remote.model.StravaBestEffortDto
import com.deivitdev.peakflow.domain.model.ActivityBestEffort

fun StravaActivityDto.toDb(): ActivityDb {
    val loc = buildString {
        if (locationCity != null) append(locationCity)
        if (locationState != null) {
            if (isNotEmpty()) append(", ")
            append(locationState)
        }
        if (locationCountry != null) {
            if (isNotEmpty()) append(", ")
            append(locationCountry)
        }
    }.takeIf { it.isNotEmpty() }
    
    val splitsStr = splitsMetric?.let { Json.encodeToString(it) }
    val bestEffortsStr = bestEfforts?.let { Json.encodeToString(it) }
    
    return ActivityDb(
        id = id.toString(),
        name = name,
        type = sportType ?: type ?: "unknown",
        distance = (distance / 1000.0), // Strava is in meters, we want km
        movingTime = movingTime.toLong(),
        elapsedTime = elapsedTime.toLong(),
        totalElevationGain = totalElevationGain.toDouble(),
        startDate = startDate,
        startDateLocal = startDateLocal,
        averageSpeed = (averageSpeed * 3.6), // Strava is in m/s, we want km/h
        maxSpeed = (maxSpeed * 3.6),
        averageHeartRate = averageHeartRate?.toDouble(),
        maxHeartRate = maxHeartRate?.toDouble(),
        calories = calories?.toDouble(),
        kilojoules = kilojoules?.toDouble(),
        sufferScore = sufferScore?.toDouble(),
        averagePower = averageWatts?.toDouble(),
        location = loc,
        polyline = map?.polyline ?: map?.summaryPolyline,
        weightedAverageWatts = weightedAverageWatts?.toDouble(),
        maxWatts = maxWatts?.toDouble(),
        deviceWatts = if (deviceWatts == true) 1L else 0L,
        averageTemp = averageTemp?.toLong(),
        elevHigh = elevHigh?.toDouble(),
        elevLow = elevLow?.toDouble(),
        deviceName = deviceName,
        gearName = gear?.name,
        gearDistance = gear?.distance,
        splitsJson = splitsStr,
        bestEffortsJson = bestEffortsStr,
        streams = null,
        z1Seconds = 0,
        z2Seconds = 0,
        z3Seconds = 0,
        z4Seconds = 0,
        z5Seconds = 0
    )
}

fun ActivityDb.toDomain(): Activity {
    val streamsObj = streams?.let {
        try {
            Json.decodeFromString<ActivityStreams>(it)
        } catch (e: Exception) {
            null
        }
    }

    val splitsList = splitsJson?.let {
        try {
            Json.decodeFromString<List<StravaSplitDto>>(it).map { split ->
                ActivitySplit(
                    distanceKm = split.distance / 1000f,
                    movingTimeSeconds = split.movingTime,
                    elevationDifference = split.elevationDifference,
                    averageSpeedKmh = split.averageSpeed * 3.6f,
                    averageHeartRate = split.averageHeartRate
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    val bestEffortsList = bestEffortsJson?.let {
        try {
            Json.decodeFromString<List<StravaBestEffortDto>>(it).map { be ->
                ActivityBestEffort(
                    name = be.name,
                    movingTimeSeconds = be.movingTime,
                    distanceMeters = be.distance
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    return Activity(
        id = id,
        name = name,
        type = when(type.lowercase()) {
            "weighttraining" -> WorkoutType.STRENGTH
            "walk" -> WorkoutType.WALKING
            "run" -> WorkoutType.RUNNING.ROAD
            "trailrun" -> WorkoutType.RUNNING.TRAIL
            "mountainbikeride" -> WorkoutType.CYCLING.MOUNTAIN
            "gravelride" -> WorkoutType.CYCLING.GRAVEL
            "roadride" -> WorkoutType.CYCLING.ROAD
            "ride", "ebikeride", "emountainbikeride" -> WorkoutType.CYCLING.GENERIC
            else -> WorkoutType.OTHER
        },
        distanceKm = distance.toFloat(),
        movingTimeSeconds = movingTime.toInt(),
        elapsedTimeSeconds = elapsedTime.toInt(),
        totalElevationGainMeters = totalElevationGain.toFloat(),
        startDate = startDate,
        startDateLocal = startDateLocal,
        averageSpeedKmh = averageSpeed.toFloat(),
        maxSpeedKmh = maxSpeed.toFloat(),
        averageHeartRate = averageHeartRate?.toFloat(),
        maxHeartRate = maxHeartRate?.toFloat(),
        calories = calories?.toFloat(),
        kilojoules = kilojoules?.toFloat(),
        sufferScore = sufferScore?.toInt(),
        averagePower = averagePower?.toFloat(),
        location = location,
        polyline = polyline,
        heartRateSeries = streamsObj?.heartRate,
        elevationSeries = streamsObj?.elevation,
        cadenceSeries = streamsObj?.cadence,
        velocitySmoothSeries = streamsObj?.velocitySmooth,
        gapSeries = streamsObj?.gradeAdjustedPace,
        wattsSeries = streamsObj?.watts,
        weightedAveragePower = weightedAverageWatts?.toFloat(),
        maxPower = maxWatts?.toFloat(),
        hasPowerMeter = deviceWatts == 1L,
        averageTemp = averageTemp?.toInt(),
        elevHigh = elevHigh?.toFloat(),
        elevLow = elevLow?.toFloat(),
        deviceName = deviceName,
        gearName = gearName,
        gearDistance = gearDistance,
        splits = splitsList,
        bestEfforts = bestEffortsList,
        z1Seconds = z1Seconds ?: 0,
        z2Seconds = z2Seconds ?: 0,
        z3Seconds = z3Seconds ?: 0,
        z4Seconds = z4Seconds ?: 0,
        z5Seconds = z5Seconds ?: 0
    )
}
