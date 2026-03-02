## 1. Data Layer - Trend Aggregation

- [ ] 1.1 Create `GetTrendsUseCase` to query all activities and aggregate metrics (distance, duration, elevation) by week and month.
- [ ] 1.2 Define new data classes for trend data points (e.g., `TrendDataPoint(date: LocalDate, value: Float)`).
- [ ] 1.3 Implement logic to group activities into weekly and monthly buckets using `kotlinx-datetime`.
- [ ] 1.4 Add `ActivityRepository` methods to efficiently retrieve activities within specific date ranges (if needed, or adapt existing `getActivities()`).

## 2. UI Components - Trend Charts

- [ ] 2.1 Create a new composable `TrendsChartCard` to display a single trend line chart.
- [ ] 2.2 Inside `TrendsChartCard`, implement a basic custom composable to draw a line graph using `Canvas`, visualizing `TrendDataPoint`s.
- [ ] 2.3 Add a segmented control (e.g., `SegmentedButton`) for selecting time granularity (Weekly/Monthly).
- [ ] 2.4 Add another segmented control for selecting the metric to display (Distance/Duration/Elevation).

## 3. Integration & Verification

- [ ] 3.1 Update `AnalyticsViewModel` to manage the state of selected granularity and metric, and call `GetTrendsUseCase` accordingly.
- [ ] 3.2 Expose the trend data from `AnalyticsViewModel` to `AnalyticsScreen`.
- [ ] 3.3 Integrate `TrendsChartCard` into `AnalyticsScreen.kt`, placing it after the `ActivityTypeBreakdownCard`.
- [ ] 3.4 Manually verify that trend graphs display correctly and update when granularity or metric is changed.
- [ ] 3.5 Test edge cases: no activities, activities spanning across year/month boundaries, very few activities.