## ADDED Requirements

### Requirement: Fetch Activity Streams
The system SHALL be able to fetch time-series data (streams) for a specific activity from Strava, including heart rate, altitude, distance, and cadence.

#### Scenario: Fetch streams for a cycling activity
- **WHEN** the detailed view for a cycling activity is opened
- **THEN** the system SHALL request heart rate and altitude streams from the Strava API

### Requirement: Extended Activity Metrics
The system SHALL retrieve and store additional performance metrics for each activity, such as average and max heart rate, calories, and kilojoules.

#### Scenario: Store extended metrics
- **WHEN** an activity is synchronized or fetched in detail
- **THEN** the system SHALL parse and persist the heart rate and energy metrics in the local database
