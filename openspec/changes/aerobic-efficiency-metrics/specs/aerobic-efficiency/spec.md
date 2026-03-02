## MODIFIED Requirements

### Requirement: Decoupling Calculation (Pw:Hr / Pa:Hr)
The system SHALL calculate the percentage of aerobic decoupling for any activity that contains both effort data (Power or Pace) and Heart Rate data.

#### Formula:
1. Divide the activity into two equal halves based on duration.
2. Calculate the Efficiency Factor (EF) for each half:
   - `EF = Average Power (or Pace) / Average Heart Rate`.
3. Calculate the Decoupling %:
   - `Decoupling = ((EF_first_half - EF_second_half) / EF_first_half) * 100`.

#### Scenario: Decoupling calculated for a steady walk
- **WHEN** a walking activity has continuous heart rate and pace data
- **THEN** the system SHALL calculate the decoupling percentage and display it in the activity detail.

### Requirement: Efficiency Status Interpretation
The system SHALL interpret the decoupling percentage to provide actionable feedback to the user.

#### Scenario: Solid Aerobic Base
- **WHEN** Decoupling is less than 5%
- **THEN** the system SHALL indicate a "STABLE" aerobic status, suggesting the user is well-adapted to this duration/intensity.

#### Scenario: Cardiovascular Drift/Fatigue
- **WHEN** Decoupling is greater than 5%
- **THEN** the system SHALL indicate a "DRIFT DETECTED" status, suggesting cardiovascular fatigue or that the intensity/duration was above current aerobic capacity.
