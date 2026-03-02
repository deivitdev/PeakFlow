## ADDED Requirements

### Requirement: Strava OAuth2 Authentication
The system SHALL implement the Strava OAuth2 flow to allow users to authorize access to their activity data.

#### Scenario: User successfully authorizes Strava
- **WHEN** the user initiates the Strava connection and completes the authorization on the Strava website
- **THEN** the system receives and securely stores the access and refresh tokens

### Requirement: Synchronize Cycling Activities
The system SHALL fetch cycling activities from the Strava API and save them to the local database.

#### Scenario: Periodic activity sync
- **WHEN** the user triggers a sync or the background task runs
- **THEN** the system retrieves new cycling activities since the last sync and persists them
