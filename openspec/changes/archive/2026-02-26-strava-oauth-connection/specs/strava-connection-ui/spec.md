## ADDED Requirements

### Requirement: Strava Connection Button
The system SHALL provide a clearly labeled "Conectar con Strava" button in the Profile/Connect section.

#### Scenario: Initiate Strava connection
- **WHEN** the user clicks the "Conectar con Strava" button
- **THEN** the system SHALL redirect the user to the Strava authorization website in an external browser

### Requirement: Connection Status Visualization
The system SHALL display the current connection status to the user.

#### Scenario: User is disconnected
- **WHEN** the system has no valid Strava tokens for the user
- **THEN** the system SHALL display the "Conectar con Strava" button

#### Scenario: User is connected
- **WHEN** the system has valid Strava tokens for the user
- **THEN** the system SHALL display a "Conectado a Strava" status indicator and a "Desconectar" option
