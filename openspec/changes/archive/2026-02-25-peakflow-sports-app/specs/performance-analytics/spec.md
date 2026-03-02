## ADDED Requirements

### Requirement: Performance Insights Dashboard
The system SHALL provide a dashboard that visualizes training volume, intensity, and trends over time.

#### Scenario: View monthly training volume
- **WHEN** the user navigates to the analytics dashboard and selects the "Monthly" view
- **THEN** the system displays a chart showing the total duration and distance of cycling and strength sessions for the current month

### Requirement: Performance Improvement Recommendations
The system SHALL analyze stored workout data to provide basic performance improvement suggestions.

#### Scenario: Identify overtraining or lack of progress
- **WHEN** the analysis engine processes the last 4 weeks of data
- **THEN** the system provides a summary of progress (e.g., "Has aumentado tu volumen de ciclismo en un 10%") and suggestions for next steps
