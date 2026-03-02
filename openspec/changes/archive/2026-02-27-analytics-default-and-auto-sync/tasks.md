## 1. Navigation Reordering

- [x] 1.1 Reorder enum entries in `Screen.kt` to move `Analytics` to the first position.
- [x] 1.2 Update the `mutableStateOf` initialization for `currentScreen` in `App.kt` to default to `Screen.Analytics`.

## 2. Automatic Synchronization

- [x] 2.1 Add a `LaunchedEffect(Unit)` in `App.kt` to trigger `repository.syncActivities()` on application startup.
- [x] 2.2 Ensure the automatic sync only runs if the user has an established Strava connection.

## 3. Verification

- [x] 3.1 Run the app and verify it opens directly to the Analytics screen.
- [x] 3.2 Verify the bottom navigation bar order is: Analytics, Workouts, Profile.
- [x] 3.3 Check logs or UI indicators to confirm that a synchronization starts automatically upon launch.
