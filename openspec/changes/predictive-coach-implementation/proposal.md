## Why

PeakFlow currently offers excellent retrospective analysis but lacks proactive guidance for the athlete's future. Implementing a Predictive Coach will transform the app into an active training partner that suggests daily load targets (TSS) and ensures a healthy sport balance, helping users avoid overtraining and optimize their progress.

## What Changes

- **Predictive Engine**: Development of a logic layer that solves the TSB (Form) equation to suggest daily TSS targets for three different development paths (Build, Maintain, Recover).
- **Daily Recommendation UI**: A new interactive section in the Insights tab showing "paths for today" with predicted outcomes.
- **Sport Balance Monitor**: Automated detection of neglected activity types (e.g., Strength) over 7 and 14-day windows.
- **Nutritional & Recovery Forecasts**: Integration of metabolic estimates into daily suggestions to provide proactive fueling advice.
- **Future Form Visualization**: Enhancement of the Fitness/Fatigue chart to show projected trends based on the selected daily target.

## Capabilities

### New Capabilities
- `predictive-coaching`: Daily load suggestions (Build/Maintain/Recover), sport balance monitoring (e.g., strength reminders), and nutritional forecasting.

### Modified Capabilities
- `fitness-fatigue-model`: Extension to support projected data points for CTL/ATL/TSB based on hypothetical future loads.
- `performance-analytics`: Integration of proactive recommendations into the main insights dashboard.

## Impact

- **Domain Layer**: New use cases for load calculation and sport balance auditing.
- **Presentation Layer**: Extension of `AnalyticsUiState` and `AnalyticsViewModel` to handle predictions.
- **UI Components**: New "Daily Path Selector" component and updates to the FitnessFatigueChart for projection rendering.
