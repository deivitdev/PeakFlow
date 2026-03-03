# Design: Migrate to Koin DI

## Module Structure
Dependencies will be organized into functional modules located in `commonMain/kotlin/com/deivitdev/peakflow/di/Modules.kt`:

1.  **DataModule**: Strava API client, SQLDelight database, Local Data Sources, and Repositories.
2.  **UseCaseModule**: All domain use cases (over 15 classes).
3.  **ViewModelModule**: All ViewModels (`Analytics`, `Profile`, `WorkoutList`, `ActivityDetail`).
4.  **PlatformModule**: Uses `expect/actual` to provide platform-specific implementations like `HealthRepository` and `SqlDriver`.

## Configuration Management
To avoid passing individual strings for Strava credentials, we will introduce a `StravaConfig` data class:
```kotlin
data class StravaConfig(
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String
)
```
This will be injected into `ActivityRepositoryImpl`.

## Initialization
- **Android**: `startKoin` will be called in a custom `Application` class or `MainActivity`, passing the `androidContext`.
- **iOS**: A helper function `initKoin` will be exported to Swift to start the Koin container.

## Compose Integration
In `App.kt`, we will replace:
```kotlin
val viewModel = remember { AnalyticsViewModel(...) }
```
with:
```kotlin
val viewModel: AnalyticsViewModel = koinViewModel()
```
This requires the `koin-compose` library.
