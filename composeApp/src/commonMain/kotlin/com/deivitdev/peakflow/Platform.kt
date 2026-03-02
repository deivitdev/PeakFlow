package com.deivitdev.peakflow

interface Platform {
    val name: String
    fun setLanguage(language: String?)
}

expect fun getPlatform(): Platform
