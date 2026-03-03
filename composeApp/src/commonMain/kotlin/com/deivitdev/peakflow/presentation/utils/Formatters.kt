package com.deivitdev.peakflow.presentation.utils

import kotlinx.datetime.*

fun formatDate(isoString: String): String {
    return try {
        val instant = Instant.parse(isoString)
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val day = localDateTime.dayOfMonth.toString().padStart(2, '0')
        val month = localDateTime.monthNumber.toString().padStart(2, '0')
        val year = localDateTime.year
        val hour = localDateTime.hour.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')
        "$day/$month/$year $hour:$minute"
    } catch (e: Exception) {
        isoString.take(16).replace("T", " ")
    }
}

fun formatDecimal(value: Float): String {
    val isNegative = value < 0
    val absValue = if (isNegative) -value else value
    val multiplier = 100
    val rounded = (absValue * multiplier).toInt()
    val integerPart = rounded / multiplier
    val fractionalPart = rounded % multiplier
    val sign = if (isNegative) "-" else ""
    return "$sign$integerPart.${fractionalPart.toString().padStart(2, '0')}"
}

fun formatSeconds(seconds: Int): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return if (h > 0) {
        "${h}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}"
    } else {
        "${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}"
    }
}

fun formatPace(speedKmh: Float): String {
    if (speedKmh <= 0f) return "--:--"
    
    val paceDecimal = 60f / speedKmh
    val minutes = paceDecimal.toInt()
    val seconds = ((paceDecimal - minutes) * 60).toInt()
    
    return "${minutes}:${seconds.toString().padStart(2, '0')}"
}
