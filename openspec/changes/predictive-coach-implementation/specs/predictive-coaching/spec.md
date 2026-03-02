## ADDED Requirements

### Requirement: Daily Load Suggestion Engine
The system SHALL calculate three daily Training Stress Score (TSS) targets based on the user's current Chronic Training Load (CTL) and Acute Training Load (ATL) to reach specific Training Stress Balance (TSB) targets for the following day.
- **Build Path**: Target tomorrow's TSB between -10 and -30.
- **Maintain Path**: Target tomorrow's TSB between 0 and -10.
- **Recover Path**: Target TSS < 20 to allow TSB to increase.

#### Scenario: Suggest daily paths
- **WHEN** the user views the predictive coaching section
- **THEN** the system SHALL display three cards representing Build, Maintain, and Recover paths with their respective TSS targets and predicted tomorrow's Form (TSB).

### Requirement: Sport Balance Auditing
The system SHALL analyze the frequency of specific activity types (e.g., STRENGTH) over a rolling 7 and 14-day window to identify imbalances.

#### Scenario: Recommend neglected activity
- **WHEN** the user has not recorded a "STRENGTH" activity in the last 7 days
- **THEN** the system SHALL add a "Priority: Strength/Mobility" recommendation to the daily training options.

### Requirement: Proactive Fueling Advice
The system SHALL estimate the carbohydrate (glycogen) demand for each suggested TSS target based on the theoretical intensity required to achieve that load.

#### Scenario: Forecast carbohydrate needs
- **WHEN** a training path is selected or viewed
- **THEN** the system SHALL display an estimated glycogen expenditure (in grams) to help the user plan their pre and post-workout nutrition.
