# Proposal: Migrate to Koin DI

## Problem
Currently, PeakFlow uses manual Dependency Injection inside `App.kt` using Compose `remember { ... }`. This has led to:
- An overgrown `App.kt` (>350 lines) that is hard to maintain.
- High coupling between the UI layer and implementation details.
- Fragile dependency graphs where adding a single parameter requires updating multiple files.
- Difficulties in implementing SOLID principles like SRP, as splitting ViewModels becomes too costly in terms of "glue code".

## Solution
Integrate **Koin**, a pragmatic lightweight dependency injection framework for Kotlin Multiplatform. This will centralize dependency management, simplify platform-specific injections (Health Connect), and allow for a clean, modular architecture.

## Scope
- Add Koin dependencies for KMP and Compose.
- Define `DataModule`, `UseCaseModule`, and `ViewModelModule`.
- Implement `startKoin` for Android and iOS.
- Refactor `App.kt` to use `koinViewModel()` and remove all manual initialization logic.

## Capabilities
- **dependency-management**: Centralized dependency injection using Koin across all platforms.
