## 1. Data Layer - Model & Mapping

- [x] 1.1 Add `RUNNING` to `WorkoutType` sealed class (including Road and Trail variants).
- [x] 1.2 Update `StravaActivityDto` to domain mapper to handle `Run` and `TrailRun` types.
- [x] 1.3 Ensure cadence data is correctly mapped for running (steps per minute).

## 2. UI Components - Running Detail

- [x] 2.1 Create `RunningDetailContent.kt` component following the "Electric Pro" style.
- [x] 2.2 Implement the running-specific Bento card (Distance, Pace, Time, Cadence).
- [x] 2.3 Implement the "Sweat & Hydration" estimation card.
- [x] 2.4 Update `ActivityDetailScreen.kt` to route to `RunningDetailContent` for running activities.

## 3. Advanced Split Visualization

- [x] 3.1 Update the split list to calculate and highlight fastest/slowest kilometers.
- [x] 3.2 Ensure splits display pace (min/km) instead of speed.

## 4. Polishing & Localization

- [x] 4.1 Add running-specific string resources (spm, sweat loss, pace labels) in English and Spanish.
- [x] 4.2 Verify layout on mobile and tablet sizes.
