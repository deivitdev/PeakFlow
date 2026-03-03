## MODIFIED Requirements

### Requirement: Actionable Training Status
The system SHALL interpret the TSB value and the Ramp Rate to provide a textual training status and injury risk warning.

#### Scenario: Positive TSB (Freshness)
- **WHEN** TSB is greater than +5
- **THEN** the system displays a status of "FRESH" or "RECOVERED", indicating high readiness for a hard effort or race.

#### Scenario: Optimal TSB (Training Zone)
- **WHEN** TSB is between -10 and -30
- **THEN** the system displays a status of "OPTIMAL TRAINING", indicating a productive workload for fitness gains.

#### Scenario: Negative TSB (Overreaching Risk)
- **WHEN** TSB is less than -30
- **THEN** the system displays a status of "OVERREACHING" and provides a warning about increased injury risk and the need for recovery.

#### Scenario: Risky Progression (Ramp Rate Alert)
- **WHEN** the weekly Ramp Rate is greater than 8
- **THEN** the system SHALL prioritize a "HIGH PROGRESSION RISK" warning even if TSB is within the optimal range.
