## ADDED Requirements

### Requirement: User can configure HR Max
The system SHALL allow the user to input and save their Maximum Heart Rate in the Profile section.

#### Scenario: Set HR Max
- **WHEN** user enters "185" in the Max HR field and saves
- **THEN** future heart rate zone calculations for that user use 185 as the 100% baseline

### Requirement: Default HR Max
The system SHALL use 190 BPM as the default baseline if the user has not configured a custom value.

#### Scenario: No custom HR Max
- **WHEN** user hasn't set an HR Max
- **THEN** zone calculations use 190 BPM
