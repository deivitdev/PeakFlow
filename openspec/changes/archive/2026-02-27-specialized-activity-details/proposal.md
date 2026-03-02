## Why

A generic activity detail screen often fails to highlight the most relevant metrics for specific sports. For example, cadence is critical for cycling but irrelevant for strength training, while sets and reps (if available) or pure duration are more important for the latter. Specialized views ensure that users get the most value out of their data without clutter or missing information.

## What Changes

- Implement four specialized detail layouts based on `WorkoutType`:
    - **CyclingDetail**: Focuses on distance, speed, elevation, power, and cadence. Features large maps and multi-metric interactive charts.
    - **WalkingDetail**: Focuses on steps (if available), distance, and duration. Emphasizes the route map.
    - **StrengthDetail**: Focuses on heart rate zones, calories, and duration. Replaces the map with an abstract "training intensity" backdrop.
    - **GenericDetail**: A fallback layout for other activity types, providing a balanced overview of all available telemetry.
- Refactor `ActivityDetailScreen` to act as a router that selects the appropriate specialized layout.
- Standardize the "HUD" aesthetic across all specialized views.

## Capabilities

### New Capabilities
- `specialized-activity-ui`: Requirements for sport-specific data visualization and layouts.

### Modified Capabilities
- `activity-detail-ui`: Update core requirements to support dynamic layout selection.

## Impact

- **UI Layer**: `ActivityDetailScreen.kt` will be broken down into multiple sub-composables.
- **Components**: New specialized cards and metric groups for each sport type.
- **Architecture**: Move from a single monolithic screen to a strategy-based UI selection.
