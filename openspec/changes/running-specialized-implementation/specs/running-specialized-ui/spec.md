## ADDED Requirements

### Requirement: Running Data Integration
The system SHALL support `RUNNING` as a first-class `WorkoutType` and map Strava's `Run` and `TrailRun` activity types accordingly.

#### Scenario: Activity Mapping
- **WHEN** a Strava activity of type `Run` is synced
- **THEN** it SHALL be stored and displayed as a `RUNNING` workout type.

### Requirement: Runner-Specific "Hero" Metrics
The system SHALL display a layout that prioritizes running-specific metrics in the main overview.

#### Scenario: Main metrics display
- **WHEN** a user opens a running activity
- **THEN** the system SHALL display:
  - **Average & Max Pace** (formatted as min/km).
  - **Running Cadence** (measured in steps per minute - spm).
  - **Total Elevation Gain** (crucial for trail runners).
  - **Estimated Sweat Loss/Hydration** (calculated based on duration and intensity).

### Requirement: Split Performance Analysis
The system SHALL provide a high-visibility list of kilometer-by-kilometer splits, highlighting the fastest and slowest segments.

#### Scenario: View splits
- **WHEN** a running activity has split data from Strava
- **THEN** the system SHALL render a list showing the time, pace, and elevation change for every kilometer.
