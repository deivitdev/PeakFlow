# strava-detailed-fetch Specification

## Purpose
TBD - created by archiving change fetch-real-strava-data. Update Purpose after archive.
## Requirements
### Requirement: Fetch Activity Streams
The system SHALL be able to fetch time-series data (streams) for a specific activity from Strava, including heart rate, altitude, distance, and cadence.

#### Scenario: Fetch streams for a cycling activity
- **WHEN** the detailed view for a cycling activity is opened
- **THEN** the system SHALL request heart rate and altitude streams from the Strava API

### Requirement: Extended Activity Metrics
The system SHALL retrieve and store additional performance metrics for each activity, such as average and max heart rate, calories, and kilojoules, along with new fields including `average_temp`, `elev_high`, `elev_low`, `max_watts`, and `device_name`.

#### Scenario: Store extended metrics
- **WHEN** an activity is synchronized or fetched in detail
- **THEN** the system SHALL parse and persist the heart rate, energy, temperature, and elevation extreme metrics in the local database.

### Requirement: Fetch Gear Details
The system SHALL retrieve and store details about the gear used for an activity, including the gear's name and its total accumulated distance.

#### Scenario: Fetch bike details with total mileage
- **WHEN** an activity is synchronized or fetched in detail
- **THEN** the system SHALL parse and persist the `gear` object, including the total distance associated with that piece of equipment.

