import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.appcompat)
            implementation(libs.sqldelight.android.driver)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.koin.android)
        }

        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.compose.material.icons)
            implementation(libs.compose.material.icons.core)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)

            // SQLDelight
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutines)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.auth)

            // KMP Maps
            implementation(libs.kmpmaps.core)
            implementation(libs.kmpmaps.google)

            // Coil
            implementation(libs.coil.compose)
            implementation(libs.coil.network)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

sqldelight {
    databases {
        create("PeakFlowDatabase") {
            packageName.set("com.deivitdev.peakflow.db")
        }
    }
}

android {
    namespace = "com.deivitdev.peakflow"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    val envFile = project.rootProject.file(".env")
    val env = Properties()
    if (envFile.exists()) {
        env.load(envFile.inputStream())
    }

    defaultConfig {
        applicationId = "com.deivitdev.peakflow"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "STRAVA_CLIENT_ID", "\"${env["STRAVA_CLIENT_ID"] ?: ""}\"")
        buildConfigField("String", "STRAVA_CLIENT_SECRET", "\"${env["STRAVA_CLIENT_SECRET"] ?: ""}\"")
        buildConfigField("String", "GOOGLE_MAPS_API_KEY", "\"${env["GOOGLE_MAPS_API_KEY"] ?: ""}\"")
        manifestPlaceholders["googleMapsApiKey"] = env["GOOGLE_MAPS_API_KEY"] ?: ""
    }
    buildFeatures {
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.compose.uiTooling)
}
