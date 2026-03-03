package com.deivitdev.peakflow

class IOSPlatform : Platform {
    override val name: String = "iOS"
    override fun setLanguage(language: String?) {
        // Implementation for iOS language change if needed
    }
}

actual fun getPlatform(): Platform = IOSPlatform()
