## MODIFIED Requirements

### Requirement: Extended Activity Metrics
The system SHALL retrieve and store additional performance metrics for each activity, such as average and max heart rate, calories, and kilojoules, along with new fields including `average_temp`, `elev_high`, `elev_low`, `max_watts`, and `device_name`.

#### Scenario: Store extended metrics
- **WHEN** an activity is synchronized or fetched in detail
- **THEN** the system SHALL parse and persist the heart rate, energy, temperature, and elevation extreme metrics in the local database.

## ADDED Requirements

### Requirement: Fetch Gear Details
The system SHALL retrieve and store details about the gear used for an activity, including the gear's name and its total accumulated distance.

#### Scenario: Fetch bike details with total mileage
- **WHEN** an activity is synchronized or fetched in detail
- **THEN** the system SHALL parse and persist the `gear` object, including the total distance associated with that piece of equipment.
