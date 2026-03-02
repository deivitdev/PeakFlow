# specialized-activity-ui Specification

## Purpose
TBD - created by archiving change specialized-activity-details. Update Purpose after archive.
## Requirements
### Requirement: Cycling-Specific Layout
The system SHALL provide a layout optimized for cycling that prioritizes power (W), cadence (RPM), and speed (KM/H) alongside distance and elevation.

#### Scenario: View cycling activity
- **WHEN** the user opens a cycling activity
- **THEN** the system SHALL display a layout with prominent power and cadence metrics and synchronized telemetry charts.

### Requirement: Strength-Specific Layout
The system SHALL provide a layout optimized for strength training that prioritizes heart rate zones, intensity metrics, and session efficiency.

#### Scenario: View strength activity
- **WHEN** the user opens a strength training activity
- **THEN** the system SHALL:
  - Replace the route map with an abstract visual backdrop.
  - Display a "Relative Effort" card using the activity's suffer score.
  - Provide a time analysis comparing moving time vs. elapsed time.
  - Highlight both average and maximum heart rate metrics.
  - Display time spent in heart rate zones.

### Requirement: Walking-Specific Layout
The system SHALL provide a layout optimized for walking that highlights distance, pace, environment, and gear.

#### Scenario: View walking activity
- **WHEN** the user opens a walking activity
- **THEN** the system SHALL:
  - Display a large route map.
  - Display distance, moving time, and average pace (min/km).
  - Display environment data including average temperature.
  - Display elevation extremes (max/min elevation).
  - Display the gear used (shoes) and its total distance.
  - Highlight rest time by comparing moving time vs elapsed time.

### Requirement: Adaptive HUD Aesthetic
All specialized layouts SHALL adhere to the "Electric Pro" design language, using high-contrast neon accents and glassmorphic containers.

#### Scenario: Aesthetic consistency
- **WHEN** any specialized layout is rendered
- **THEN** it SHALL use the same color palette and typography as the rest of the application.

