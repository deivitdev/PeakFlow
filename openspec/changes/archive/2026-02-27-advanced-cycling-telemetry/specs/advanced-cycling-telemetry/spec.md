## ADDED Requirements

### Requirement: Advanced Power Metrics
The system SHALL display Weighted Average Watts (Normalized Power) and Maximum Watts for activities where power data is available.

#### Scenario: Display power consistency
- **WHEN** a cycling activity with a power meter is viewed
- **THEN** the system SHALL show "Normalized Power" and "Max Power" metrics in the data grid.

### Requirement: Equipment Attribution
The system SHALL identify and display the specific bicycle (Gear) used for the activity.

#### Scenario: Identify used bike
- **WHEN** an activity has a `gear_id` and name provided by Strava
- **THEN** the system SHALL display the bike name (e.g., "Trek Emonda") in the activity header.

### Requirement: Kilometer Splits Analysis
The system SHALL provide a breakdown of performance for each kilometer (split), including pace/speed and elevation difference.

#### Scenario: View split table
- **WHEN** the user scrolls to the bottom of a cycling activity detail
- **THEN** the system SHALL render a list of all 1km splits showing time and vertical gain/loss for each.

### Requirement: Power Source Verification
The system SHALL indicate whether the power data is from a dedicated sensor (device watts) or estimated by Strava.

#### Scenario: Verify sensor data
- **WHEN** `device_watts` is true
- **THEN** the system SHALL display a "Real Power" badge or icon next to the wattage.
