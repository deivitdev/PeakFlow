## ADDED Requirements

### Requirement: Chronic Training Load (CTL) / Fitness
The system SHALL calculate the CTL, representing the user's long-term fitness level. CTL is an exponentially weighted moving average of daily Training Stress Score (TSS) over a 42-day period.

#### Scenario: CTL calculation
- **WHEN** the system processes activity data for the last 42 days
- **THEN** it calculates the average daily TSS, giving more weight to recent days, to derive the current Fitness (CTL) value.

### Requirement: Acute Training Load (ATL) / Fatigue
The system SHALL calculate the ATL, representing the user's short-term fatigue level. ATL is an exponentially weighted moving average of daily Training Stress Score (TSS) over a 7-day period.

#### Scenario: ATL calculation
- **WHEN** the system processes activity data for the last 7 days
- **THEN** it calculates the average daily TSS, giving significant weight to the most recent activities, to derive the current Fatigue (ATL) value.

### Requirement: Training Stress Balance (TSB) / Form
The system SHALL calculate the TSB, representing the user's current readiness or "form". TSB is calculated as the difference between the previous day's CTL and the previous day's ATL.

#### Scenario: TSB calculation
- **WHEN** CTL and ATL values are available for the previous day
- **THEN** the system calculates `TSB = CTL_yesterday - ATL_yesterday` to derive the current Form (TSB) value.

### Requirement: Actionable Training Status
The system SHALL interpret the TSB value to provide a textual training status and injury risk warning.

#### Scenario: Positive TSB (Freshness)
- **WHEN** TSB is greater than +5
- **THEN** the system displays a status of "FRESH" or "RECOVERED", indicating high readiness for a hard effort or race.

#### Scenario: Optimal TSB (Training Zone)
- **WHEN** TSB is between -10 and -30
- **THEN** the system displays a status of "OPTIMAL TRAINING", indicating a productive workload for fitness gains.

#### Scenario: Negative TSB (Overreaching Risk)
- **WHEN** TSB is less than -30
- **THEN** the system displays a status of "OVERREACHING" and provides a warning about increased injury risk and the need for recovery.

### Requirement: Performance Trend Visualization
The system SHALL provide a multi-line chart visualizing CTL, ATL, and TSB trends over a configurable time period (e.g., 3 months).

#### Scenario: View Fitness vs Fatigue chart
- **WHEN** the user navigates to the detailed performance insights
- **THEN** the system renders a chart with three distinct lines representing Fitness (CTL), Fatigue (ATL), and Form (TSB) to show how they interact over time.
