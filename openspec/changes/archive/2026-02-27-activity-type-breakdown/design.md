## Context

The Analytics tab currently displays overall aggregated metrics (total distance, time, elevation, activity count). With the recent introduction of a more granular `WorkoutType` sealed class, there's an opportunity to provide users with a detailed breakdown of their training across different disciplines.

## Goals / Non-Goals

**Goals:**
- Implement logic in `GetPerformanceMetricsUseCase` to aggregate activity data (count, distance, duration) by each `WorkoutType`.
- Design and implement a new UI section within `AnalyticsScreen` to display this breakdown.
- Represent the breakdown using clear visual components (e.g., bar charts or a list with progress indicators).

**Non-Goals:**
- This design will not include filtering the breakdown by date ranges (e.g., last month's breakdown). The initial implementation will be for all-time data.
- It will not introduce advanced chart interactions beyond displaying static data.

## Decisions

1.  **Data Aggregation:**
    *   `GetPerformanceMetricsUseCase` will be modified to return a new data structure (`ActivityTypeBreakdown` or similar) containing maps or lists of aggregated metrics (count, distance, duration) for each `WorkoutType` (including specific cycling subtypes like `CYCLING.ROAD`, `CYCLING.MOUNTAIN`).
    *   The aggregation will iterate through all stored `Activity` objects and sum up the relevant metrics based on their `WorkoutType`.

2.  **UI Representation:**
    *   A new composable, `ActivityTypeBreakdownCard`, will be created to encapsulate the display logic.
    *   This card will contain a list of `ActivityTypeBreakdownItem`s, each representing a `WorkoutType`.
    *   Each item will display:
        *   An icon representative of the `WorkoutType`.
        *   The `WorkoutType` name (e.g., "Road Cycling").
        *   Total `distanceKm` and `totalDurationSeconds` for that type.
        *   A simple progress bar or text indicating its proportion relative to the total activity or distance.

3.  **Chart Choice:**
    *   Initially, a simple list-based display with text and perhaps a `LinearProgressIndicator` for proportionality will be used due to time constraints and the complexity of integrating a full charting library.
    *   Future iterations could explore dedicated charting libraries for richer visualizations (e.g., `MPAndroidChart` or `Compose-Charts`).

## Risks / Trade-offs

-   **[Risk] Performance Impact**: Aggregating data for a very large number of activities could impact performance.
    -   **Mitigation**: The current `GetPerformanceMetricsUseCase` already processes all activities. We will monitor performance and consider optimizations (e.g., database-level aggregation, caching) if issues arise.
-   **[Trade-off] Simple Visualization**: Opting for a list-based UI over a full chart initially.
    -   **Reasoning**: Prioritizes delivering core breakdown functionality quickly, while keeping the door open for richer charting in the future. The `MetricProCard` component is flexible enough to accommodate this.
