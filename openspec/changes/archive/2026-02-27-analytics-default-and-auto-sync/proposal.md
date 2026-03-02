## Why

The app currently defaults to the workout list upon opening, which may not be the most relevant entry point for users focused on their overall progress. Furthermore, requiring manual synchronization to see new data creates friction; automatic syncing on launch ensures that performance metrics and trends are always up-to-date.

## What Changes

- **Default Home Screen**: The "Analytics" screen will now be the default destination when the app opens, replacing the "Workouts" screen.
- **Automatic Strava Sync**: The system will automatically trigger a Strava synchronization process on app launch (cold start) if the user has an active Strava connection.
- **Bottom Bar Reordering**: The "Analytics" tab will be moved to the first position in the bottom navigation bar.

## Capabilities

### New Capabilities
- None.

### Modified Capabilities
- `strava-sync`: Add a requirement for automatic background synchronization upon app initialization/foregrounding when authenticated.
- `activity-list-ui`: Modify the navigation behavior so it is no longer the default entry point.

## Impact

- **Navigation**: Reordering of bottom navigation items in `App.kt` or the navigation controller.
- **Initialization**: Adding a sync trigger in the main application entry point or the `AnalyticsViewModel` init block.
- **User Experience**: Immediate access to performance data upon opening the app.
