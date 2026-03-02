## Context

The current architecture fetches detailed activity data but only maps a subset of the available performance metrics from Strava. The UI uses a generic `WorkoutType` enum that doesn't account for the richer `sport_type` data, and the `UserProfile` model lacks fields for athletic benchmarks like FTP, preventing advanced performance calculations.

## Goals / Non-Goals

**Goals:**
- Expand the data layer (`DTO`, domain model, and persistence) to include new performance and gear telemetry.
- Refactor the `ActivityDetailScreen` to use a hierarchical `WorkoutType` structure that maps to Strava's `sport_type`.
- Implement new Bento cards for the added telemetry and a dedicated "Gear" slot.
- Add an `ftpWatts` field to the `UserProfile` and implement local IF/TSS calculations.

**Non-Goals:**
- This design will not implement a full "gear closet" management UI. It will only display the gear used for the specific activity.
- It will not fetch historical data to backfill these new metrics for all activities in the database.

## Decisions

1.  **Data Model Expansion**:
    *   **StravaActivityDto**: Add `average_temp`, `elev_high`, `elev_low`, `max_watts`, `kilojoules`, and `device_name`. The `StravaGearDto` will be updated to include `distance`.
    *   **Activity (Domain Model)**: Mirror the new fields from the DTO.
    *   **ActivityEntity (Database)**: Add corresponding columns to the local database table.
    *   **UserProfile (Domain & DB)**: Add `ftpWatts: Int?`.

2.  **Hierarchical WorkoutType**:
    *   Refactor the `WorkoutType` enum into a `sealed class` to represent the hierarchy.
    *   `sealed class WorkoutType`
    *   `data object Strength : WorkoutType()`
    *   `data object Walking : WorkoutType()`
    *   `sealed class Cycling : WorkoutType()`
        *   `data object Road : Cycling()`
        *   `data object Mountain : Cycling()`
        *   `data object Gravel : Cycling()`
        *   `data object GenericCycle : Cycling()`
    *   A mapper function will be created in the data layer to convert Strava's `sport_type` string into this sealed class structure.

3.  **UI Implementation**:
    *   The `ActivityDetailScreen` will use a `when` statement on the `WorkoutType` sealed class.
    *   The `CyclingDetailContent` will have a nested `when` to handle `Road`, `Mountain`, etc., allowing for different "Hero" metric arrangements.
    *   New composables (`MetricItem`, `GearCard`) will be created to display the new data points in the Bento HUD.

4.  **Performance Calculations**:
    *   A new use case, `CalculatePerformanceMetricsUseCase`, will be created.
    *   It will take an `Activity` and a `UserProfile` as input.
    *   It will return a data class containing calculated `ifValue` and `tssValue`, which will be displayed in the UI if the required data is present.

## Risks / Trade-offs

-   **[Risk]** Strava API changes could break the mapping for `sport_type` or other new fields.
    -   **Mitigation**: The mapping logic will be centralized and include a fallback to a generic type (`GenericCycle`, `OTHER`) to prevent crashes.
-   **[Trade-off]** Calculating TSS/IF locally adds complexity.
    -   **Reasoning**: This provides a valuable "Pro" feature that Strava often puts behind a paywall, adding unique value to PeakFlow.
