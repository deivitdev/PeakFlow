## Why

Currently, the application uses mocked data for performance charts and limited attributes for activities. Users need accurate, high-fidelity data directly from Strava to perform meaningful performance analysis. Fetching real streams (time-series data) and extended activity details is essential for the app to be a serious training tool.

## What Changes

- **Extended API Integration**: Implement fetching of detailed activity data and streams (heart rate, altitude, distance, etc.) from the Strava API.
- **Data Model Expansion**: Update DTOs and Domain models to include fields like `average_heartrate`, `max_heartrate`, `kilojoules`, and `suffer_score`.
- **Database Schema Update**: Enhance SQLDelight schema to persist extended activity details and serialized time-series data.
- **Improved Synchronization**: Increase the sync limit and implement logic to fetch detailed data for newly synchronized activities.
- **Telemetry Integration**: Replace random data generation in the detail view with actual time-series data fetched from Strava.

## Capabilities

### New Capabilities
- `strava-detailed-fetch`: Logic for fetching deep activity data and time-series streams.

### Modified Capabilities
- `strava-sync`: Updated to handle higher volumes of data and more complex data structures.

## Impact

- **External APIs**: Increased usage of Strava API endpoints (Activity Detail and Activity Streams).
- **Data Layer**: Significant updates to `StravaApiClient`, `ActivityRepositoryImpl`, and `LocalDataSource`.
- **Performance**: Need to manage storage and processing of time-series data efficiently.
