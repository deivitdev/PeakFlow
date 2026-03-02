## 1. Data Layer Expansion

- [x] 1.1 Update `Activity` table schema with `weightedAverageWatts`, `maxWatts`, `deviceWatts`, `gearName`, and `splitsJson`
- [x] 1.2 Implement database migration `7.sqm`
- [x] 1.3 Update `StravaActivityDto` to include the new fields from the Strava API
- [x] 1.4 Refine `Mappers.kt` to handle the new cycling metrics

## 2. UI Component Development

- [x] 2.1 Create `SplitsList` component to display kilometer-by-kilometer data
- [x] 2.2 Add \"Power Badge\" logic to show if data is from a real sensor
- [x] 2.3 Update `DetailHeroCard` to support sub-metrics (e.g., showing NP and Max under Avg Power)

## 3. Screen Integration

- [x] 3.1 Integrate Gear name into `ActivityHeader`
- [x] 3.2 Update `CyclingDetailContent` with the new Power and Splits sections
- [x] 3.3 Verify layout behavior when data is missing (e.g., no power meter)
