## MODIFIED Requirements

### Requirement: Synchronize Cycling Activities
The system SHALL fetch cycling activities from the Strava API and save them to the local database.

#### Scenario: Periodic activity sync with increased depth
- **WHEN** the user triggers a sync
- **THEN** the system retrieves up to 100 recent activities and ensures all available summary metrics are persisted
