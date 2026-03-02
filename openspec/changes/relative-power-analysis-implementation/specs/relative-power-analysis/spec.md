## ADDED Requirements

### Requirement: W/kg Calculation
The system SHALL calculate the relative power (W/kg) for any activity or profile state that contains both power data (Watts) and user weight (kg).

#### Scenario: FTP-based Relative Power
- **WHEN** the user has both `ftpWatts` and `weightKg` set in their profile
- **THEN** the system SHALL calculate their "Relative FTP" (e.g., 3.2 W/kg).

### Requirement: Performance Category Classification
The system SHALL categorize the user's performance level based on their Relative FTP (W/kg) using standard cycling categories.

#### Scenario: User level classification
- **WHEN** the Relative FTP is calculated
- **THEN** the system SHALL assign a category:
  - < 2.0 W/kg: RECREATIONAL
  - 2.0 - 3.0 W/kg: INTERMEDIATE
  - 3.0 - 4.0 W/kg: ADVANCED
  - 4.0 - 5.0 W/kg: ELITE
  - > 5.0 W/kg: WORLD CLASS

### Requirement: Dynamic Performance Targets
The system SHALL provide targets to help users reach the next performance category.

#### Scenario: View targets for improvement
- **WHEN** the user is in an INTERMEDIATE category
- **THEN** the system SHALL display the required FTP increase or weight reduction needed to reach the ADVANCED category.
