## Context

The application now has a data layer capable of fetching and storing Strava activities. We need to implement the UI component that bridges the local database with the user, providing a clear and localized view of their workouts.

## Goals / Non-Goals

**Goals:**
- Provide a performant list view using Jetpack Compose.
- Implement the "Workouts" screen as the default starting screen.
- Integrate the existing `SyncActivitiesUseCase` into the UI via pull-to-refresh.
- Ensure the UI adheres to the established Material 3 theme and Spanish localization.

**Non-Goals:**
- Navigation to a detail view (out of scope for this change).
- Filtering or searching (can be added later).
- Infinite scrolling (we will fetch a reasonable amount of recent data).

## Decisions

### 1. UI Component: `LazyColumn`
- **Rationale**: standard and efficient way to display lists in Compose, handling item recycling and smooth scrolling.
- **Alternatives**: `Column` with scrolling (rejected due to poor performance with large lists).

### 2. State Management: `StateFlow` in ViewModel
- **Rationale**: consistent with existing patterns in the project. The ViewModel will observe the local database and expose a UI state object.
- **Alternatives**: `LiveData` (rejected as it's not multiplatform-friendly).

### 3. Interaction: `PullRefresh` (Material 3)
- **Rationale**: provides a native Android/iOS feel for manual data synchronization.
- **Alternatives**: A dedicated sync button (rejected to keep the UI clean).

### 4. Presentation Logic: Mapper-based Formatting
- **Rationale**: the ViewModel or a specialized Mapper will format technical metrics (e.g., duration in seconds) into human-readable strings (e.g., "1h 30m") localized to Spanish.

## Risks / Trade-offs

- **[Risk] Sync Performance** → **Mitigation**: ensure the UI remains responsive during synchronization by using a loading state and running the sync in a background coroutine.
- **[Risk] Resource Overload** → **Mitigation**: the `LazyColumn` will only render visible items, minimizing the memory footprint.
