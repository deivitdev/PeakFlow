## Context

The PeakFlow application currently initializes with the "Workouts" screen and requires users to manually refresh or pull-to-refresh to see new activities from Strava. The navigation order and default screen are defined in `Screen.kt` and `App.kt`.

## Goals / Non-Goals

**Goals:**
- Make "Analytics" the first screen shown to the user.
- Ensure "Analytics" is the leftmost item in the bottom navigation bar.
- Automatically synchronize activities from Strava on app launch if the user is connected.

**Non-Goals:**
- Implementing periodic background sync while the app is closed.
- Changing the layout of the screens themselves.

## Decisions

### Navigation Order and Default Screen
- **Decision**: Reorder the `Screen` enum entries in `Screen.kt` so `Analytics` comes before `Workouts`.
- **Rationale**: The `App.kt` uses `Screen.entries` to build the `NavigationBar`, so changing the enum order is the most idiomatic way to reorder the tabs.
- **Decision**: Update the initial state of `currentScreen` in `App.kt` from `Screen.Workouts` to `Screen.Analytics`.

### Automatic Synchronization
- **Decision**: Add a `LaunchedEffect(Unit)` in `App.kt` that checks if the user is connected to Strava and triggers `repository.syncActivities()`.
- **Rationale**: `App.kt` is the root of the UI and has access to the repository and user state. Triggering it once on launch ensures data is fresh immediately.
- **Alternative**: Triggering in `AnalyticsViewModel`. However, `App.kt` already handles some connection logic and `LaunchedEffect(Unit)` is appropriate for one-time initialization.

## Risks / Trade-offs

- **[Risk] Rate Limiting**: Automatic sync on every launch could hit Strava API limits if a user opens/closes the app frequently. → **Mitigation**: Future iterations could add a "last sync" timestamp check to avoid syncing more than once every X minutes.
- **[Risk] Battery/Data usage**: Syncing on every launch uses more resources. → **Mitigation**: The sync is relatively lightweight (summary data only) and only occurs if the user is connected.
