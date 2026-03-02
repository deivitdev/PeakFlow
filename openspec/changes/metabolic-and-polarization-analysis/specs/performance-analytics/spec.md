## MODIFIED Requirements

### Requirement: Performance Insights Dashboard
The system SHALL provide a dashboard that visualizes training volume, intensity, and trends over time. This dashboard SHALL integrate:
- Visualizations for historical trends as defined in the `analytics-trends` capability.
- Aggregated metrics broken down by activity type.
- Fitness vs. Fatigue (CTL/ATL/TSB) metrics and charts as defined in the `fitness-fatigue-model` capability.
- **NEW**: Polarization ratio visualizations (80/20 distribution) for 7 and 28-day windows as defined in `polarization-analysis`.
- **NEW**: Metabolic oxidation trends (Carbs vs. Fats) for recent activities as defined in `metabolic-analysis`.

#### Scenario: View monthly training volume
- **WHEN** the user navigates to the analytics dashboard and selects the "Monthly" view
- **THEN** the system displays a chart showing the total duration and distance of cycling and strength sessions for the current month, along with a breakdown by specific activity types and time-series trend data.

#### Scenario: View fitness-fatigue status
- **WHEN** the user views the main analytics dashboard
- **THEN** the system SHALL display the current training status (e.g., "Optimal Training") derived from the TSB value.

### Requirement: Performance Improvement Recommendations
The system SHALL analyze stored workout data to provide basic performance improvement suggestions. This analysis will consider:
- The distribution of activity types and historical trends, as defined in the `analytics-breakdown` and `analytics-trends` capabilities.
- The user's current training status and form balance as defined in the `fitness-fatigue-model`.
- **NEW**: The polarization ratio and the presence of "Grey Zone" overtraining alerts as defined in `polarization-analysis`.
- **NEW**: The estimated glycogen depletion and recovery needs as defined in `metabolic-analysis`.

#### Scenario: Identify overtraining or lack of progress
- **WHEN** the analysis engine processes the last 4 weeks of data and the current fitness-fatigue balance
- **THEN** the system provides a summary of progress and actionable recommendations (e.g., "Tu fatiga es alta, considera un día de descanso para evitar lesiones" or "Estás pasando mucho tiempo en la Zona Gris, haz tus sesiones suaves más lentas") based on trend analysis, the fitness-fatigue model, and polarization metrics.
