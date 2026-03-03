## ADDED Requirements

### Requirement: Weekly Ramp Rate Calculation
The system SHALL calculate the Ramp Rate as the numerical difference between the current Chronic Training Load (CTL) and the CTL value from 7 days prior.

#### Scenario: Successful Ramp Rate calculation
- **WHEN** the system generates the fitness and fatigue data
- **THEN** it SHALL compare the latest CTL value with the value recorded exactly 7 data points back in history to produce the Ramp Rate.

### Requirement: Progression Safety Classification
The system SHALL classify the Ramp Rate into four safety zones based on the weekly point increase:
- **Maintenance**: < 2 points/week
- **Productive**: 2 to 5 points/week
- **Intense**: 5 to 8 points/week
- **Risk**: > 8 points/week

#### Scenario: Classify productive growth
- **WHEN** the calculated Ramp Rate is 4.2
- **THEN** the system SHALL classify the status as "Productive" and assign a corresponding success color (e.g., Green or Blue).

### Requirement: High Progression Alert
The system SHALL provide a clear visual warning when the user's Ramp Rate enters the "Risk" zone (> 8 points/week).

#### Scenario: Alert for risky progression
- **WHEN** the user's Ramp Rate is 9.5
- **THEN** the system SHALL display a "High Risk" status with a warning color (e.g., Red) and a tooltip recommending caution.
