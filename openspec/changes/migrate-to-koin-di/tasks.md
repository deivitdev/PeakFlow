# Tasks: Migrate to Koin DI

## Phase 1: Dependencies & Setup
- [x] Add Koin versions to `gradle/libs.versions.toml` (`koin-core`, `koin-android`, `koin-compose`).
- [x] Add Koin dependencies to `composeApp/build.gradle.kts` for `commonMain` and `androidMain`.
- [x] Create `StravaConfig` data class in `domain/model`.

## Phase 2: Define Modules
- [x] Implement `dataModule` in `commonMain/di`.
- [x] Implement `useCaseModule` in `commonMain/di`.
- [x] Implement `viewModelModule` in `commonMain/di`.
- [x] Implement `expect val platformModule: Module` in `commonMain`.
- [x] Implement `actual val platformModule` for Android (providing `SqlDriver` and `HealthRepository`).
- [x] Implement `actual val platformModule` for iOS.

## Phase 3: Platform Initialization
- [x] Configure `startKoin` in `MainActivity.kt` for Android.
- [x] Create `initKoin` helper in `iosMain` for Swift initialization.

## Phase 4: Refactor App.kt
- [x] Replace all `remember { UseCase(...) }` with Koin modules.
- [x] Replace `remember { ViewModel(...) }` with `koinViewModel()`.
- [x] Remove unused parameters from `App` function (DI will handle them).
- [x] Verify build and dependency graph.
