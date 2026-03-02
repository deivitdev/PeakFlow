## Why

Users need a detailed view of their individual activities to analyze their performance more closely. Currently, the application only provides a list view and a high-level analytics dashboard, but lacks the depth required for post-workout review.

## What Changes

- Implement a new `ActivityDetailScreen` that is triggered when an activity is selected from the list.
- Display comprehensive activity data including name, date, type, distance, duration, and elevation.
- Integrate visual charts to represent performance metrics over time (e.g., speed or heart rate if available).
- Add navigation support from the `WorkoutListScreen` to the `ActivityDetailScreen`.

## Capabilities

### New Capabilities
- `activity-detail-ui`: Requirements for the activity detail interface, including data representation and charts.

### Modified Capabilities
<!-- No requirement changes to existing specs -->

## Impact

- **UI Layer**: New screen implementation and ViewModel for activity details.
- **Navigation**: Update navigation logic to handle arguments (activity ID).
- **Visualization**: Integration of a charting library (e.g., a simple Compose-based chart).
