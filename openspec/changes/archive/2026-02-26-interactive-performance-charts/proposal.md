## Why

Athletes need more dynamic ways to visualize their performance data during an activity. Static charts provide a basic overview, but interactive charts allow users to pinpoint specific moments in their workout to understand the relationship between effort (heart rate) and terrain (elevation).

## What Changes

- Implement interactive line charts for heart rate and elevation in the activity detail screen.
- Add touch interaction to the charts to display specific data point details (values and timestamps) via tooltips or a synchronized HUD overlay.
- Synchronize the cursor between the heart rate and elevation charts to allow simultaneous comparison of metrics.

## Capabilities

### New Capabilities
- `interactive-charts`: Requirements for interactive data visualization components, including touch handling and data point inspection.

### Modified Capabilities
<!-- No requirement changes to existing specs -->

## Impact

- **UI Layer**: Update `ActivityDetailScreen` to include the new interactive chart components.
- **Components**: Create a reusable `InteractiveTelemetryChart` using Compose `Canvas` and pointer input handling.
- **Domain Layer**: Ensure activity data includes time-series information for heart rate and elevation.
