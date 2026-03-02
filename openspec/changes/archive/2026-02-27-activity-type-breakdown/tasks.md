## 1. Data Layer - Aggregation

- [x] 1.1 Create a new data class `ActivityTypeMetrics` to hold aggregated data (count, distance, duration) for a specific `WorkoutType`.
- [x] 1.2 Modify `PerformanceMetrics` to include a `Map<WorkoutType, ActivityTypeMetrics>` for the breakdown.
- [x] 1.3 Update `GetPerformanceMetricsUseCase` to aggregate activities by `WorkoutType` and populate the new map in `PerformanceMetrics`.
- [x] 1.4 Ensure all `WorkoutType.CYCLING` subtypes (ROAD, MOUNTAIN, GRAVEL, GENERIC) are aggregated into a single `WorkoutType.CYCLING.GENERIC` entry for the breakdown.

## 2. UI Components - Analytics Screen

- [x] 2.1 Create a new composable `ActivityTypeBreakdownCard` which accepts a `Map<WorkoutType, ActivityTypeMetrics>` and total metrics.
- [x] 2.2 Design `ActivityTypeBreakdownCard` to display each `WorkoutType` with its name, total distance, and total duration.
- [x] 2.3 Add a `LinearProgressIndicator` or similar visual cue to each item, showing its proportion of the total distance or time.
- [x] 2.4 Integrate `ActivityTypeBreakdownCard` into `AnalyticsScreen.kt`.

## 3. Integration & Verification

- [x] 3.1 Update `AnalyticsViewModel` to expose the new `ActivityTypeMetrics` map to the UI.
- [x] 3.2 Ensure `AnalyticsScreen` correctly consumes and displays the aggregated data.
- [x] 3.3 Manually verify that the breakdown correctly reflects the activities in the database.
- [x] 3.4 Test edge cases, e.g., no activities, only one type of activity.