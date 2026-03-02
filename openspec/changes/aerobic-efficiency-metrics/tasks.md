## 1. Data Layer - Stream Expansion

- [x] 1.1 Update `ActivityStreams` DTO to include `velocity_smooth` and `watts`.
- [x] 1.2 Update `StravaApiClient.getStreams` to request `velocity_smooth` and `watts` keys.
- [x] 1.3 Update `ActivityRepositoryImpl` to parse and map the new streams.
- [x] 1.4 Update `Activity` domain model and SQLDelight schema to store the new streams.

## 2. Domain Layer - Efficiency Logic

- [x] 2.1 Update `CalculatePerformanceMetricsUseCase` to include aerobic decoupling calculation.
- [x] 2.2 Implement splitting logic for time-series data (first half vs. second half).
- [x] 2.3 Define `AerobicStatus` enum and update `PerformanceMetrics` data class.

## 3. UI Layer - Efficiency Visualization

- [x] 3.1 Create `AerobicEfficiencyCard` component with color-coded status.
- [x] 3.2 Add the card to `CyclingDetailContent.kt`.
- [x] 3.3 Add the card to `WalkingDetailContent.kt`.
- [x] 3.4 Add necessary string resources for efficiency labels and statuses.

## 4. Verification

- [x] 4.1 Verify that velocity and power streams are correctly fetched and persisted.
- [x] 4.2 Manually verify decoupling calculation for a sample activity.
- [x] 4.3 Check UI responsiveness and localized strings.
