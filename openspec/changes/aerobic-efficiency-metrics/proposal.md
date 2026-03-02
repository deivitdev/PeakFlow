## Why

To transform the app into a proactive training partner, we need to provide scientific insights into the user's cardiovascular adaptation. Aerobic Efficiency (Decoupling) is a key indicator of base fitness and readiness for increased training volume. By measuring how the heart rate responds to constant output over time, we can provide actionable feedback on whether a user's aerobic engine is stable or fatiguing prematurely.

## What Changes

- **Data Fetching**: Update Strava API requests to include `velocity_smooth` and `watts` streams for granular intensity analysis.
- **Metric Calculation**: Implement the Aerobic Decoupling formula (Pw:Hr / Pa:Hr) in the domain layer.
- **UI Enhancement**: Add an "Aerobic Efficiency" section to Cycling and Walking detail screens, including the decoupling percentage and a status indicator (Stable vs. Drift Detected).
- **Domain Model**: Update `Activity` and `PerformanceMetrics` to carry decoupling data.

## Capabilities

### New Capabilities
- None.

### Modified Capabilities
- `aerobic-efficiency`: Implement the core logic and UI defined in the base specification.
- `specialized-activity-ui`: Integrate the new efficiency metric into specialized layouts.

## Impact

- **API Layer**: `StravaApiClient` and `ActivityRepositoryImpl` will handle additional stream types.
- **Presentation Layer**: New card component for aerobic efficiency.
- **Performance**: Negligible impact as calculations are performed on existing/newly fetched stream data during activity load.
