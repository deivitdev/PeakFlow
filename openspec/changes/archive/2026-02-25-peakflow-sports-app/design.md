## Context

The project is a Kotlin-based application using Compose for the UI. Currently, it has a basic structure with an `androidMain` source set. This change introduces a significant feature set: Strava integration, local persistence, and performance analytics.

## Goals / Non-Goals

**Goals:**
- Implement a modular architecture (Clean Architecture) to separate concerns.
- Integrate Strava OAuth2 and Activity synchronization.
- Provide a localized Spanish UI with a modern Material 3 theme.
- Ensure all code-level entities and structures are in English.
- Use SOLID principles throughout the implementation.

**Non-Goals:**
- Real-time GPS tracking (activities are synced from Strava, not recorded in-app).
- Social features (comments, likes) beyond basic synchronization.
- Complex ML-based coaching (only basic trend analysis for now).

## Decisions

### 1. Architecture: Clean Architecture + MVVM
- **Rationale**: Provides clear separation between UI, Domain (Business Logic), and Data (External APIs/Persistence).
- **Alternative**: MVC or simple layered architecture. Rejected because they become hard to maintain as features grow.

### 2. Persistence: SQLDelight
- **Rationale**: Type-safe SQL for Kotlin. Excellent for multiplatform if the project expands.
- **Alternative**: Room or Realm. Rejected Room to maintain potential multiplatform compatibility and Realm for its overhead.

### 3. Networking: Ktor
- **Rationale**: Multiplatform-ready, lightweight, and works seamlessly with Kotlin Serialization.
- **Alternative**: Retrofit. Rejected as it is JVM-specific.

### 4. UI: Jetpack Compose with Material 3
- **Rationale**: Modern, declarative UI framework with excellent support for theming and localization.
- **Alternative**: Views-based XML. Rejected as it is legacy.

### 5. Localization: Multiplatform Resources (moko-resources or similar)
- **Rationale**: Standard way to manage strings and assets in a type-safe manner for multi-platform projects.
- **Alternative**: Hardcoded strings (rejected) or basic Android resources (rejected to favor multiplatform readiness).

## Risks / Trade-offs

- **[Risk] Strava API Rate Limiting** → **Mitigation**: Implement background sync with reasonable intervals and local caching to minimize API calls.
- **[Risk] Data Privacy** → **Mitigation**: Store OAuth tokens securely using EncryptedSharedPreferences (Android) or equivalent.
- **[Risk] Sync Complexity** → **Mitigation**: Use a robust sync logic that handles pagination and partial failures gracefully.
