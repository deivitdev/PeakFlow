## Context

The current application has a list of activities and a summary dashboard. We need a way to drill down into a specific activity. This involves fetching a single activity by ID and rendering it with specialized UI components for both text and graphical data.

## Goals / Non-Goals

**Goals:**
- Implement `ActivityDetailScreen` with a clean, "Glassmorphic Bento HUD" aesthetic.
- Use a charting solution (e.g., simple Canvas-based drawing or a library like KoalaPlot if available, otherwise custom Canvas logic).
- Fetch specific activity data from the `ActivityRepository`.
- Maintain consistent Spanish localization and the "Electric Pro" theme.

**Non-Goals:**
- Social sharing of specific activities.
- Editing activity details (view-only for now).
- Map integration (GPS path visualization is out of scope for this task).

## Decisions

### 1. Navigation: ID-based Routing
- **Rationale**: Passing the full activity object is fragile. Passing the ID and fetching it in the detail ViewModel ensures fresh data and cleaner navigation logic.
- **Implementation**: Update the navigation enum or add a details route that accepts a `String` ID.

### 2. UI Layout: Scrollable Bento Sections
- **Rationale**: Consistent with the existing design language. Metrics will be grouped in themed cards.
- **Components**:
    - **Header**: Name and Date.
    - **Hero Cards**: Large distance and time metrics.
    - **Chart Section**: A dedicated card for visual trends.
    - **Details Grid**: Smaller cards for elevation, speeds, and types.

### 3. Visualization: Custom Canvas Charts
- **Rationale**: For a prototype, custom Canvas logic provides full control over the aesthetic (neon glows, specific fonts) without adding heavy dependencies.
- **Implementation**: Create a `TelemetryChart` component using `Canvas`.

## Risks / Trade-offs

- **[Risk] Performance with Complex Charts** → **Mitigation**: Keep data points reasonable and optimize `Canvas` drawing logic.
- **[Risk] Navigation State** → **Mitigation**: Use a simple state-based navigation if a full router isn't yet in place, ensuring the "Back" button correctly resets the state.
