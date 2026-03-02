## 1. Data Layer Enhancements

- [x] 1.1 Add `selectSummedZonesInRange` query to `PeakFlowDatabase.sq` to aggregate time-in-zones by date.
- [x] 1.2 Update `LocalDataSource` to expose the new aggregation query.
- [x] 1.3 Add `getAggregatedZones(startDate, endDate)` to `ActivityRepository`.

## 2. Domain Models & Use Cases

- [x] 2.1 Create `MetabolicSummary` and `PolarizationSummary` domain models.
- [x] 2.2 Implement `CalculateMetabolicOxidationUseCase` with the piecewise linear model (Carbs/Fats).
- [x] 2.3 Implement `CalculatePolarizationRatioUseCase` to determine the 80/20 distribution.
- [x] 2.4 Implement `GetAdvancedPerformanceInsightsUseCase` to coordinate the analysis engine.

## 3. Analytics ViewModels & Recommendations

- [x] 3.1 Expand `AnalyticsUiState` to include polarization and metabolic data.
- [x] 3.2 Update `AnalyticsViewModel` to load and calculate the new metrics on initialization.
- [x] 3.3 Add recommendation logic for "Grey Zone" alerts (Z3 > 40%).
- [x] 3.4 Add hydration recommendation logic based on `averageTemp` and intensity.

## 4. UI Components & Visualizations

- [x] 4.1 Create `PolarizationChart` (Horizontal Stacked Bar) component for 80/20 view.
- [x] 4.2 Create `MetabolicSignatureCard` component showing Carb vs. Fat grams.
- [x] 4.3 Create `AnalyticsRecommendationCard` to display proactive alerts and hydration advice.
- [x] 4.4 Integrate new cards into the `AnalyticsScreen` layout.

## 5. Verification & Testing

- [x] 5.1 Unit test `CalculateMetabolicOxidationUseCase` with various intensity distributions.
- [x] 5.2 Unit test `CalculatePolarizationRatioUseCase` for accuracy in different time windows.
- [x] 5.3 Verify that recommendations are correctly suppressed when `averageTemp` or `ftpWatts` are missing.
