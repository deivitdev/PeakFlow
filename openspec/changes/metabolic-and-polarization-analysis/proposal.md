## Why

The app currently lacks advanced physiological and strategic training analysis. Implementing metabolic and polarization metrics will provide users with professional-level insights into their energy expenditure (Carbs vs. Fats), hydration needs, and training effectiveness (80/20 model).

## What Changes

- **Metabolic Analysis Engine**: Implementation of logic to estimate fuel usage (Carbohydrates vs. Fats) based on heart rate and power zones.
- **Hydration Recommendation System**: Proactive hydration advice based on activity intensity (IF), duration, and ambient temperature.
- **Polarization Reporting**: Weekly and monthly views of training intensity distribution to verify adherence to the 80/20 model.
- **Grey Zone Alerts**: Automated detection and user notification when too much time is spent in the inefficient Zone 3 (Tempo/Threshold).
- **Analytics Dashboard Update**: Integration of these new metrics into the existing performance visualizations.

## Capabilities

### New Capabilities
- `metabolic-analysis`: Estimation of Fat/Carb oxidation, recovery windows, and hydration needs based on intensity and environment.
- `polarization-analysis`: Analysis of 80/20 intensity distribution over 7 and 28 days, including "Grey Zone" overtraining detection.

### Modified Capabilities
- `performance-analytics`: Integration of metabolic and polarization metrics into the high-level performance dashboard.

## Impact

- **Data Layer**: `ActivityRepository` will need to expose aggregated data for polarization analysis.
- **Domain Layer**: New use cases for metabolic estimation and polarization calculation.
- **Presentation Layer**: `AnalyticsViewModel` and `AnalyticsScreen` will be expanded to display the new metrics and charts.
- **UI Components**: New charts for 80/20 distribution and metabolic breakdown.
