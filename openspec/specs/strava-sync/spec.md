# strava-sync Specification

## Purpose
TBD - created by archiving change peakflow-sports-app. Update Purpose after archive.
## Requirements
### Requirement: Strava OAuth2 Authentication
The system SHALL implement the Strava OAuth2 flow to allow users to authorize access to their activity data.

#### Scenario: User successfully authorizes Strava
- **WHEN** the user initiates the Strava connection and completes the authorization on the Strava website
- **THEN** the system receives and securely stores the access and refresh tokens

### Requirement: Synchronize Cycling Activities
The system SHALL fetch cycling activities from the Strava API and save them to the local database.

#### Scenario: Periodic activity sync with increased depth
- **WHEN** the user triggers a sync
- **THEN** the system retrieves up to 100 recent activities and ensures all available summary metrics are persisted

### Requirement: Automatic Background Synchronization
The system SHALL trigger a background synchronization with Strava upon application startup if the user is currently connected to Strava.

#### Scenario: Automatic sync on launch
- **WHEN** the app is launched and Strava is connected
- **THEN** the system triggers `SyncActivitiesUseCase` without user intervention.

