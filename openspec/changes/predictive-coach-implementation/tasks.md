## 1. Domain & Predictive Logic

- [x] 1.1 Create `TrainingRecommendation` and `PathOption` domain models.
- [x] 1.2 Implement `PredictTrainingLoadUseCase` to calculate TSS targets for Build/Maintain/Recover paths.
- [x] 1.3 Implement `AuditSportBalanceUseCase` to detect activity type neglect (rolling 7-day).
- [x] 1.4 Create `GetPredictiveCoachingUseCase` to coordinate load and balance analysis.

## 2. Analytics ViewModel Integration

- [x] 2.1 Expand `AnalyticsUiState` to include `predictiveRecommendations` and `selectedPathIndex`.
- [x] 2.2 Update `AnalyticsViewModel` to load recommendations and handle path selection.
- [x] 2.3 Modify `GetFitnessFatigueUseCase` or create a projection helper to generate future data points.

## 3. UI Components (Insights Tab)

- [x] 3.1 Create `DailyPathSelector` component (Horizontal Pager or Card Row).
- [x] 3.2 Create `PathOptionCard` to display TSS, predicted TSB, and priority sport.
- [x] 3.3 Create `FuelingForecastCard` to show estimated glycogen expenditure for the selected path.
- [x] 3.4 Integrate path selector at the top of the `InsightsContent` in `AnalyticsScreen`.

## 4. Enhanced Visualizations

- [x] 4.1 Update `FitnessFatigueChart` to support rendering dashed "projection" segments.
- [x] 4.2 Add animation for switching between different projected paths.

## 5. i18n & Verification

- [x] 5.1 Add strings for Build/Maintain/Recover paths and advice in English and Spanish.
- [x] 5.2 Unit test the load prediction math for edge cases (zero activities, extreme fatigue).
- [x] 5.3 Verify that the sport balance auditor correctly identifies missing activity types.
