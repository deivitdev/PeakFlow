## ADDED Requirements

### Requirement: Detailed Data Display
The system SHALL display the full set of attributes for a specific activity, including name, workout type, distance, moving time, elapsed time, total elevation gain, start date, and speeds.

#### Scenario: View activity details
- **WHEN** the user selects an activity from the workout list
- **THEN** the system SHALL display a detailed view with all activity metrics correctly formatted

### Requirement: Performance Charts
The system SHALL provide visual charts (e.g., line charts) to represent performance trends for the activity, such as speed variations or intensity over the duration of the workout.

#### Scenario: View speed chart
- **WHEN** the user opens the activity detail screen
- **THEN** the system SHALL render a chart showing speed data points over the course of the activity

### Requirement: Navigation and Back Support
The system SHALL allow users to navigate back to the workout list from the detail screen.

#### Scenario: Return to list
- **WHEN** the user clicks the "Back" button on the activity detail screen
- **THEN** the system SHALL navigate back to the previous screen (Workout List)
