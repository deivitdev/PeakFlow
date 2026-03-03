## 1. Domain Logic & Models

- [x] 1.1 Create `RampRateStatus` enum and `RampRateData` data class in `AnalysisModels.kt`.
- [x] 1.2 Update `FitnessFatigueData` domain model to include an optional `rampRate` field.
- [x] 1.3 Implement the Ramp Rate calculation logic in `GetFitnessFatigueUseCase.kt`.
- [x] 1.4 Add safety classification logic to map Ramp Rate values to `RampRateStatus`.

## 2. i18n & Localization

- [x] 2.1 Add Ramp Rate status strings (Maintenance, Productive, Intense, Risky) to `strings.xml`.
- [x] 2.2 Add Ramp Rate status strings to `values-es/strings.xml`.
- [x] 2.3 Add educational description for Ramp Rate to the `MetricInfoTooltip` system.

## 3. UI Components

- [x] 3.1 Create `RampRateIndicator` component (a visual gauge or styled chip).
- [x] 3.2 Update `TrainingStatusCard` in `FitnessFatigueSection.kt` to display the Ramp Rate.
- [x] 3.3 Ensure the indicator is theme-aware (colors for light/dark mode).

## 4. Verification

- [x] 4.1 Unit test the Ramp Rate calculation with a known history of CTL values.
- [x] 4.2 Verify that the classification logic correctly identifies the "Risk" zone (> 8 points).
- [x] 4.3 Verify UI rendering with various Ramp Rate values (null, low, high).
