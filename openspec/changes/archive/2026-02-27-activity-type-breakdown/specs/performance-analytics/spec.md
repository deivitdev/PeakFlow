## MODIFIED Requirements

### Requirement: Performance Insights Dashboard
The system SHALL provide a dashboard that visualizes training volume, intensity, and trends over time. This dashboard SHALL include aggregated metrics broken down by activity type, as defined in the `analytics-breakdown` capability.

#### Scenario: View monthly training volume
- **WHEN** the user navigates to the analytics dashboard and selects the "Monthly" view
- **THEN** the system displays a chart showing the total duration and distance of cycling and strength sessions for the current month, along with a breakdown by specific activity types.

### Requirement: Performance Improvement Recommendations
The system SHALL analyze stored workout data to provide basic performance improvement suggestions. This analysis will consider the distribution of activity types, as defined in the `analytics-breakdown` capability.

#### Scenario: Identify overtraining or lack of progress
- **WHEN** the analysis engine processes the last 4 weeks of data
- **THEN** the system provides a summary of progress (e.g., "Has aumentado tu volumen de ciclismo en un 10%") and suggestions for next steps, which may be informed by the activity type breakdown.