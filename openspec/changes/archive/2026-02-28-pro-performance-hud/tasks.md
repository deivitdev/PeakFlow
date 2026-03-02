## 1. Data Layer Expansion

- [ ] 1.1 Update `StravaActivityDto` to include `average_temp`, `elev_high`, `elev_low`, `max_watts`, `kilojoules`, and `device_name`.
- [ ] 1.2 Update `StravaGearDto` to include the `distance` field.
- [ ] 1.3 Update the `Activity` domain model to mirror the new fields from the DTOs.
- [ ] 1.4 Update the `ActivityEntity` and database mappings to persist the new fields.
- [ ] 1.5 Update the `ActivityRepository` mapping logic to correctly transform the new DTO fields to the domain model.

## 2. Athlete Profile & Performance Calculations

- [ ] 2.1 Update the `UserProfile` domain model and entity to include `ftpWatts: Int?`.
- [ ] 2.2 Add an input field in the `ProfileScreen` for users to enter and save their FTP.
- [ ] 2.3 Create `CalculatePerformanceMetricsUseCase` to compute IF and TSS.
- [ ] 2.4 Update `ActivityDetailViewModel` to call the new use case and expose the calculated metrics to the UI.

## 3. UI Layer Refactoring & Enhancements

- [ ] 3.1 Refactor the `WorkoutType` enum into a `sealed class` with nested objects for `Road`, `Mountain`, `Gravel`, etc.
- [ ] 3.2 Implement the mapper function in the data layer to convert Strava's `sport_type` string to the new `WorkoutType` sealed class.
- [ ] 3.3 Update `ActivityDetailScreen` to use a `when` statement on the new sealed class to select specialized layouts.
- [ ] 3.4 Create a `GearCard` composable to display the gear name and its total distance.
- [ ] 3.5 Add new `MetricItem` instances in `CyclingDetailContent` (and other relevant screens) to display the new telemetry (`average_temp`, `elev_high`, `max_watts`, etc.).
- [ ] 3.6 Add UI elements to display the calculated IF and TSS values when available.

## 4. Final Integration & Verification

- [ ] 4.1 Verify that the correct "Hero" metrics are displayed for different cycling `sport_type` values.
- [ ] 4.2 Confirm that all new data fields are correctly displayed in the UI.
- [ ] 4.3 Test the FTP input and ensure IF/TSS calculations are displayed correctly.
