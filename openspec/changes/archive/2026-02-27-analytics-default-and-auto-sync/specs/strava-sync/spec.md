## ADDED Requirements

### Requirement: Automatic Background Synchronization
The system SHALL trigger a background synchronization with Strava upon application startup if the user is currently connected to Strava.

#### Scenario: Automatic sync on launch
- **WHEN** the app is launched and Strava is connected
- **THEN** the system triggers `SyncActivitiesUseCase` without user intervention.
