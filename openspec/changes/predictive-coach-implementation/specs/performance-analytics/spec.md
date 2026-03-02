## MODIFIED Requirements

### Requirement: Performance Insights Dashboard
The system SHALL provide a dashboard that visualizes training volume, intensity, and trends over time. This dashboard SHALL integrate:
- Visualizations for historical trends as defined in the `analytics-trends` capability.
- Aggregated metrics broken down by activity type.
- Fitness vs. Fatigue (CTL/ATL/TSB) metrics and charts as defined in the `fitness-fatigue-model` capability.
- **NEW**: Predictive coaching cards showing daily training options and their predicted impact on Form (TSB) as defined in `predictive-coaching`.

#### Scenario: View monthly training volume
- **WHEN** the user navigates to the analytics dashboard and selects the "Monthly" view
- **THEN** the system displays a chart showing the total duration and distance of cycling and strength sessions for the current month, along with a breakdown by specific activity types and time-series trend data.

#### Scenario: View fitness-fatigue status
- **WHEN** the user views the main analytics dashboard
- **THEN** the system SHALL display the current training status (e.g., "Optimal Training") derived from the TSB value.
