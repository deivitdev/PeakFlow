## Why

Users need to understand their training distribution across different activity types to identify trends, ensure balanced training, and make informed decisions about their fitness goals. The current analytics view lacks this high-level breakdown.

## What Changes

- Introduce a new section in the Analytics tab dedicated to activity type breakdown.
- Display the total distance, total time, and count of activities for each `WorkoutType` (e.g., Road Cycling, Mountain Biking, Walking, Strength).
- Use a visual representation (e.g., a bar chart or pie chart) to show the proportion of each activity type.

## Capabilities

### New Capabilities
- `analytics-breakdown`: Provides aggregated statistics and visualization for activity distribution by type.

### Modified Capabilities
- `performance-analytics`: The existing performance analytics will be augmented with type-specific breakdowns.

## Impact

- `AnalyticsViewModel` will need new logic to aggregate activities by `WorkoutType`.
- `AnalyticsScreen` will require new UI components for displaying the breakdown (e.g., charts, lists).
- `ActivityRepository` may need new query methods to efficiently retrieve data for type-based aggregation.