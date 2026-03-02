## Why

To provide a professional-grade analysis for cyclists, the application needs to go beyond simple averages. Normalized power (NP), maximum power outputs, gear tracking, and split-by-split analysis are essential metrics for serious training and performance tracking.

## What Changes

- **Advanced Power Analytics**: Integration of weighted average watts (Normalized Power), max watts, and power sensor verification.
- **Equipment Tracking**: Identification and display of the specific bicycle used for the activity.
- **Split Breakdown**: Implementation of a kilometer-by-kilometer performance table (splits).
- **Extended API Data**: Update Strava integration to fetch `weighted_average_watts`, `max_watts`, `device_watts`, `gear`, and `splits_metric`.

## Capabilities

### New Capabilities
- `advanced-cycling-telemetry`: Requirements for deep cycling data analysis, including normalized power and splits.

### Modified Capabilities
- `specialized-activity-ui`: Update the cycling layout to include these new data points.

## Impact

- **Data Layer**: Significant expansion of `StravaActivityDto`, `Activity` domain model, and database schema.
- **UI Layer**: New components for split lists and power highlights in `CyclingDetailContent`.
- **Logic**: Calculation or extraction of fastest splits and power consistency.
