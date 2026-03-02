## 1. Domain Layer - Logic Implementation

- [x] 1.1 Create `CalculateRelativePowerUseCase` to handle W/kg and Category classification.
- [x] 1.2 Update `PerformanceMetrics` (domain) to include `avgWkg` and `maxWkg` optional fields.
- [x] 1.3 Update `CalculatePerformanceMetricsUseCase` to integrate W/kg calculation for activities.

## 2. Presentation - Profile Screen

- [x] 2.1 Create `PerformanceLevelCard` component for the Profile screen.
- [x] 2.2 Update `ProfileUiState` and `ProfileViewModel` to include relative power and category data.
- [x] 2.3 Implement the \"Targets for Improvement\" logic in the ViewModel.

## 3. Presentation - Activity Detail

- [x] 3.1 Update `CyclingDetailContent` to display W/kg as sub-metrics in the Bento card.
- [x] 3.2 Ensure the UI handles activities with missing power data (hide W/kg).

## 4. Localization & Verification

- [x] 4.1 Add strings for performance categories (Recreational, Intermediate, etc.) in English and Spanish.
- [x] 4.2 Add labels for \"Relative Power\" and \"Level Improvement\" targets.
- [x] 4.3 Verify W/kg calculations against manual samples.
