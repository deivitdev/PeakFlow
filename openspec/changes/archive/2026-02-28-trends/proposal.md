## Why

Users need to visualize their training progress and volume over time to identify long-term trends, assess training effectiveness, and make data-driven adjustments to their fitness routines. The current analytics view provides only aggregated totals but lacks a time-series perspective.

## What Changes

- Introduce a new section in the Analytics tab for visualizing trends.
- Display time-series graphs for key metrics such as total distance, total duration, and total elevation gain, grouped by week or month.
- Allow users to select the time granularity (e.g., weekly, monthly).

## Capabilities

### New Capabilities
- `analytics-trends`: Provides functionality to display and interact with time-series data for athletic performance metrics.

### Modified Capabilities
- `performance-analytics`: The existing performance analytics will be augmented with trend visualizations.

## Impact

- `AnalyticsViewModel` will need new logic to query and aggregate activities over time periods.
- `AnalyticsScreen` will require new UI components for displaying interactive charts.
- `ActivityRepository` may need new query methods to efficiently retrieve activities within specific date ranges.