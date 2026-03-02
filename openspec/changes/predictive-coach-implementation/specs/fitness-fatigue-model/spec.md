## MODIFIED Requirements

### Requirement: Performance Trend Visualization
The system SHALL provide a multi-line chart visualizing CTL, ATL, and TSB trends over a configurable time period (e.g., 3 months). 
- **NEW**: The chart SHALL support rendering "projections" (dashed lines) from the current day forward based on a hypothetical selected load.

#### Scenario: View Fitness vs Fatigue chart with projection
- **WHEN** the user navigates to the detailed performance insights and selects a future training path
- **THEN** the system renders a chart with three distinct lines representing Fitness (CTL), Fatigue (ATL), and Form (TSB) to show how they interact over time, including a projected segment for the next 24-48 hours.
