# workout-management Specification

## Purpose
TBD - created by archiving change peakflow-sports-app. Update Purpose after archive.
## Requirements
### Requirement: Local Workout Storage
The system SHALL provide a local database to store activity details for both cycling and strength training.

#### Scenario: Save new strength training session
- **WHEN** the user manually enters a strength training session with exercises, sets, and weights
- **THEN** the system saves the session to the local database

### Requirement: Spanish User Interface
The system SHALL display all user-facing labels, menus, and messages in Spanish.

#### Scenario: User opens the application
- **WHEN** the application starts
- **THEN** all UI elements are presented in Spanish (e.g., "Entrenamientos", "Sincronizar", "Perfil")

