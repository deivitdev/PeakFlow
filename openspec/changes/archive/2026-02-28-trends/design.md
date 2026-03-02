## Context

The Analytics tab currently provides aggregated overall and type-specific metrics. To provide deeper insights, users need to visualize how these metrics change over time. This requires aggregating data not just across all activities, but within specific time windows (e.g., weekly, monthly).

## Goals / Non-Goals

**Goals:**
- Implement data aggregation logic for time-series metrics (distance, duration, elevation) by week and month.
- Design and integrate UI components in `AnalyticsScreen` to display interactive line charts.
- Provide a user interface to select the time granularity (weekly/monthly).

**Non-Goals:**
- This design will not include advanced forecasting or predictive analytics.
- It will not initially support custom date range selections beyond predefined weekly/monthly views.
- It will not integrate a full-fledged chart library; a lightweight solution will be prioritized for initial implementation.

## Decisions

1.  **Data Aggregation Strategy:**
    *   A new use case, `GetTrendsUseCase`, will be created. It will accept `granularity` (WEEKLY/MONTHLY) and a `metric` (DISTANCE, DURATION, ELEVATION) as parameters.
    *   `GetTrendsUseCase` will query all activities from `ActivityRepository`.
    *   Activities will be grouped by week or month based on their `startDate`.
    *   For each time period, the selected `metric` will be summed.
    *   The use case will return a list of data points (e.g., `(Date, Value)`).

2.  **UI Component for Trends:**
    *   A new composable, `TrendsChartCard`, will be created. It will accept the trend data (list of `(Date, Value)`), selected metric, and granularity.
    *   Inside `TrendsChartCard`, a simple custom composable will draw a line chart. This might initially be a basic `Canvas` drawing to represent data points and lines.
    *   A segment control or radio button group will allow users to switch between "Weekly" and "Monthly" granularity.
    *   Another segment control will allow users to switch between metrics (Distance, Duration, Elevation).

3.  **Integration into AnalyticsScreen:**
    *   `TrendsChartCard` will be placed in `AnalyticsScreen.kt`, likely below the activity type breakdown.
    *   `AnalyticsViewModel` will manage the state for selected granularity and metric, and call `GetTrendsUseCase` accordingly.

## Risks / Trade-offs

-   **[Risk] Performance for large datasets**: Aggregating and drawing many data points could be slow on older devices.
    -   **Mitigation**: Implement caching for aggregated trend data. Ensure efficient database queries by `startDate` range. Consider downsampling data for very long periods.
-   **[Trade-off] Basic Charting**: Using a custom, simple chart instead of a feature-rich library.
    -   **Reasoning**: Reduces initial development complexity and dependency footprint. Can be easily replaced with a more advanced library in future iterations if required. This allows us to focus on data aggregation and core UI first.
-   **[Risk] Date Handling Complexity**: Grouping by week/month across different time zones or year boundaries can be tricky.
    -   **Mitigation**: Standardize all date calculations to UTC to avoid timezone issues. Leverage `kotlinx-datetime` for robust date manipulation.
