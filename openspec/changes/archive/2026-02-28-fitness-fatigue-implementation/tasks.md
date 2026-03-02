## 1. Data Layer & Calculations

- [x] 1.1 Add `DailyLoad` table to SQLDelight to store aggregated daily TSS.
- [x] 1.2 Implement TSS fallback estimation logic for activities without power data (using `sufferScore`).
- [x] 1.3 Create a `SyncDailyLoadUseCase` to populate the `DailyLoad` table from existing activities.
- [x] 1.4 Implement the EWMA algorithm for CTL and ATL calculation in a new domain service or use case.

## 2. Domain & Presentation Integration

- [x] 2.1 Define domain models for `FitnessFatigueData` (Fitness, Fatigue, Form, Status).
- [x] 2.2 Create `GetFitnessFatigueUseCase` to retrieve calculated values for a specific time range.
- [x] 2.3 Update `AnalyticsUiState` to include fitness-fatigue metrics.
- [x] 2.4 Update `AnalyticsViewModel` to call the new use case and manage training status logic.

## 3. UI Components

- [x] 3.1 Create `TrainingStatusCard` to display the interpreted TSB (Fresh, Optimal, Overreaching).
- [x] 3.2 Implement `FitnessFatigueChart` using custom `Canvas` drawing to visualize CTL, ATL, and TSB lines.
- [x] 3.3 Integrate the new card and chart into the `AnalyticsScreen`.

## 4. Verification & Localization

- [x] 4.1 Add string resources for all training statuses and labels in English and Spanish.
- [x] 4.2 Verify calculation accuracy against a spreadsheet or external tool.
- [x] 4.3 Ensure the UI handles empty states and "Building Data" phases gracefully.
