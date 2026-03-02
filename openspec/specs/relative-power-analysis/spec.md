# relative-power-analysis Specification

## Purpose
Relative Power Analysis (Watts per Kilogram) is the industry standard for measuring athletic potential and performance in weight-bearing sports, particularly cycling and walking/running. By integrating the user's weight from their profile with power data from activities, the system provides a normalized metric that allows for fair comparison across different body types and helps track improvements in body composition and strength.

## Requirements

### Requirement: W/kg Calculation
The system SHALL calculate the relative power (W/kg) for any activity or profile state that contains both power data (Watts) and user weight (kg).

#### Formula:
- `Relative Power = Average Power (or FTP) / User Weight`.

#### Scenario: FTP-based Relative Power
- **WHEN** the user has both `ftpWatts` and `weightKg` set in their profile
- **THEN** the system SHALL calculate their "Relative FTP" (e.g., 3.2 W/kg).

### Requirement: Performance Category Classification
The system SHALL categorize the user's performance level based on their Relative FTP (W/kg) using standard cycling categories (Coggan Scale).

#### Scenario: User level classification
- **WHEN** the Relative FTP is calculated
- **THEN** the system SHALL assign a category:
  - `< 2.0 W/kg`: RECREATIONAL
  - `2.0 - 3.0 W/kg`: INTERMEDIATE (Cat 4/5)
  - `3.0 - 4.0 W/kg`: ADVANCED (Cat 2/3)
  - `4.0 - 5.0 W/kg`: ELITE (Cat 1)
  - `> 5.0 W/kg`: WORLD CLASS (Pro)

### Requirement: Dynamic Performance Targets
The system SHALL provide "What-if" scenarios to help users reach the next performance category.

#### Scenario: View targets for improvement
- **WHEN** the user is in an INTERMEDIATE category
- **THEN** the system SHALL display the required FTP increase or weight reduction needed to reach the ADVANCED category.

### Requirement: Relative Effort in Activity Detail
The system SHALL display the W/kg for specific segments or entire sessions in the activity detail view.

#### Scenario: View session intensity
- **WHEN** a cycling activity has power data
- **THEN** the system SHALL display the `Average W/kg` and `Max W/kg` for that session.
