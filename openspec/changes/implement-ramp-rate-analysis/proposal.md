## Why

Currently, PeakFlow tracks fitness level (CTL) but doesn't inform the user about their speed of progression. Increasing training load too rapidly is a primary cause of sports injuries. The Ramp Rate metric will provide users with a "speedometer" for their fitness growth, helping them identify if their progression is healthy, productive, or risky.

## What Changes

- **Domain Logic**: Implementation of the Ramp Rate calculation (CTL today minus CTL 7 days ago).
- **UI Enhancement**: A new "Improvement Speed" or "Ramp Rate" indicator in the Insights tab, specifically within the Fitness & Fatigue section.
- **Safety Alerts**: Visual status indicators (Maintenance, Productive, Intense, Risk) based on the calculated Ramp Rate value.
- **Tooltip Support**: Educational content explaining the Ramp Rate and its significance for training safety.

## Capabilities

### New Capabilities
- `ramp-rate-analysis`: Calculation, classification, and visualization of the weekly fitness progression speed.

### Modified Capabilities
- `fitness-fatigue-model`: Extension of the data model to include the Ramp Rate value and its safety status.
- `performance-analytics`: Integration of the Ramp Rate metric into the performance dashboard.

## Impact

- **Domain Layer**: `GetFitnessFatigueUseCase` will be updated to compute the Ramp Rate based on the generated history.
- **Presentation Layer**: `AnalyticsUiState` and `FitnessFatigueData` will be expanded to hold the new metric.
- **UI Components**: `FitnessFatigueSection` will be updated to display the Ramp Rate gauge or status.
