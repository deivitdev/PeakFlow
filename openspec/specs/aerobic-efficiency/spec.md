# aerobic-efficiency Specification

## Purpose
Aerobic Efficiency (also known as Aerobic Decoupling or Pw:Hr) measures the stability of a user's aerobic base. It quantifies how much the heart rate increases to maintain a constant output (power or pace) during a session. A low decoupling percentage indicates a strong aerobic engine, while a high percentage suggests cardiovascular fatigue or lack of base fitness.

## Requirements

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

### Requirement: Historical Efficiency Trend
The system SHALL track the Efficiency Factor (EF) over time for similar activity types to visualize improvements in cardiovascular fitness.

#### Scenario: View efficiency trend
- **WHEN** the user views their performance analytics for the last 90 days
- **THEN** the system SHALL show a trend line of EF, where an upward trend indicates improved fitness (more output for the same heart rate).
