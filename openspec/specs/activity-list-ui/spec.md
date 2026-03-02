# activity-list-ui Specification

## Purpose
TBD - created by archiving change list-strava-activities. Update Purpose after archive.
## Requirements
### Requirement: Display Activity List
The system SHALL display a scrollable list of activities stored in the local database. Each item MUST show the activity name, type, distance, and duration.

#### Scenario: User views the workout list
- **WHEN** the user navigates to the "Workouts" screen
- **THEN** the system displays all stored activities sorted by date (newest first)

### Requirement: Activity Type Visualization
The system SHALL display distinct icons for different activity types (e.g., a bike for cycling, a dumbbell for strength).

#### Scenario: Visual differentiation of activities
- **WHEN** the list is rendered
- **THEN** each activity item includes an icon corresponding to its `WorkoutType`

### Requirement: Refresh Activity Data
The system SHALL allow the user to trigger a manual synchronization with Strava via a "Pull-to-refresh" gesture.

#### Scenario: Successful manual sync
- **WHEN** the user performs a pull-to-refresh gesture on the workouts screen
- **THEN** the system triggers the `SyncActivitiesUseCase` and updates the list with new data

