## ADDED Requirements

### Requirement: Time-Series Data Visualization
The system SHALL display interactive time-series graphs for key performance metrics within the Analytics tab.

#### Scenario: View weekly distance trend
- **WHEN** the user navigates to the Analytics tab and selects "Weekly" granularity
- **THEN** the system SHALL display a line graph showing total distance accumulated per week over a configurable period (e.g., last 12 weeks).

#### Scenario: View monthly duration trend
- **WHEN** the user navigates to the Analytics tab and selects "Monthly" granularity
- **THEN** the system SHALL display a line graph showing total duration accumulated per month over a configurable period (e.g., last 12 months).

### Requirement: Configurable Time Granularity
The user SHALL be able to select the time granularity for the trend graphs (e.g., weekly, monthly).

#### Scenario: Switch to monthly view
- **WHEN** the user selects "Monthly" from a granularity selector
- **THEN** the trend graphs SHALL update to display data aggregated by month.