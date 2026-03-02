## 1. Utilities & Infrastructure

- [x] 1.1 Implement `formatPace(speedKmh: Float)` in `Formatters.kt`.
- [x] 1.2 Add necessary string resources for walking (Pace, Min/Km, Average Pace, Max Pace, Rest Time, etc.) in English and Spanish.

## 2. Component Enhancements

- [x] 2.1 Update `GearCard.kt` to accept an optional `icon: ImageVector` parameter.
- [x] 2.2 Update `CyclingDetailContent.kt` to pass the bike icon to `GearCard`.

## 3. Walking Screen Updates

- [x] 3.1 Update `WalkingDetailContent.kt` to display the new "Hero" metrics (Pace).
- [x] 3.2 Implement the \"Environment & Elevation\" section in `WalkingDetailContent.kt`.
- [x] 3.3 Add the `GearCard` to `WalkingDetailContent.kt` when gear is available, using the walking icon.
- [x] 3.4 Ensure the \"Performance Telemetry\" section correctly handles the new metrics if applicable.

## 4. Verification

- [x] 4.1 Verify walking activities show pace (min/km) instead of just speed.
- [x] 4.2 Confirm temperature and elevation extremes are displayed correctly.
- [x] 4.3 Verify the `GearCard` shows the walk icon and correct shoe distance.
- [x] 4.4 Check that rest time is calculated correctly (Elapsed - Moving).
