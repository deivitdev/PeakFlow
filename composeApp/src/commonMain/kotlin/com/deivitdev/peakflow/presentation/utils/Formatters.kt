package com.deivitdev.peakflow.presentation.utils

fun formatDecimal(value: Float): String {
    val multiplier = 100
    val rounded = (value * multiplier).toInt()
    val integerPart = rounded / multiplier
    val fractionalPart = rounded % multiplier
    return "$integerPart.${fractionalPart.toString().padStart(2, '0')}"
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
