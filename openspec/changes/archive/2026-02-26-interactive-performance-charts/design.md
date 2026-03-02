## Context

The current `ActivityDetailScreen` uses a static `TelemetryChart`. We need to evolve this into an interactive experience. The user wants to see heart rate and elevation data points by touching the chart.

## Goals / Non-Goals

**Goals:**
- Create a reusable `InteractiveTelemetryChart` component.
- Implement touch/drag detection to update a shared "inspection state".
- Synchronize multiple charts so the cursor appears on all of them simultaneously.
- Display a floating HUD or tooltip with precise data.

**Non-Goals:**
- Zooming or panning (fixed time range for now).
- Multiple metric selection (only heart rate and elevation).

## Decisions

### 1. Reusable Component: `InteractiveTelemetryChart`
- **Rationale**: We need to display two different metrics (HR and Elevation) with the same interactive behavior.
- **Implementation**: The component will take a list of data points and a `MutableState<Int?>` representing the currently hovered index.

### 2. Touch Handling: `pointerInput` with `detectDragGestures`
- **Rationale**: Using Compose's `pointerInput` allows for low-level control over touch position. We can calculate the nearest data point index based on the touch X-coordinate.
- **Logic**: `hoverIndex = (touchX / totalWidth * dataPoints.size).toInt()`.

### 3. Synchronization: Shared Hover State
- **Rationale**: To achieve synchronized cursors, the `ActivityDetailScreen` will maintain a single `hoveredIndex` state and pass it to both the HR and Elevation charts.

### 4. Visuals: Glassmorphic Overlay
- **Rationale**: Consistent with the "Electric Pro" theme. The tooltip/HUD will use a semi-transparent surface with neon accents.

## Risks / Trade-offs

- **[Risk] Touch Jitter** → **Mitigation**: Implement a small dead-zone or snapping logic to the nearest integer index to ensure the UI feels stable.
- **[Risk] Performance with large datasets** → **Mitigation**: Downsample data points if the activity is very long (e.g., more than 1000 points).
