## Why

The current activity detail screen provides a generic overview of workouts. Performance-oriented users need deeper telemetry (temperature, max elevation, peak power) and contextual information about their equipment (gear mileage) to better analyze their training and maintain their machines.

## What Changes

- **Extended Data Mapping**: Update the Strava API integration to fetch and store `average_temp`, `elev_high`, `elev_low`, `max_watts`, `kilojoules`, and `device_name`.
- **Specialized Sport Logic**: Refactor the UI to display different "Hero" metrics based on the `sport_type` (e.g., Elevation for MTB, Power for Road).
- **Gear HUD Slot**: Display the specific bike/gear used along with its total accumulated mileage from Strava.
- **Athlete Profile Expansion**: Add `ftpWatts` to the User Profile to enable local intensity calculations.
- **Calculated Metrics**: Implement local calculation of Intensity Factor (IF) and TSS based on weighted average power and user FTP.

## Capabilities

### New Capabilities
- `performance-calculations`: Logic for computing IF, TSS, and other derived power/heart rate metrics.

### Modified Capabilities
- `activity-detail-ui`: Update layouts to include new performance bento cards and specialized sport-type views.
- `user-profile`: Add athletic performance fields (FTP, Max HR) to the profile model and UI.
- `strava-detailed-fetch`: Update the DTOs and mapping logic to include the new telemetry fields from Strava.

## Impact

- `StravaActivityDto` and `Activity` domain models.
- `UserProfile` domain model and persistence.
- `ActivityDetailScreen` and specialized content composables (`CyclingDetailContent`).
- `ActivityRepository` and mapping logic.
