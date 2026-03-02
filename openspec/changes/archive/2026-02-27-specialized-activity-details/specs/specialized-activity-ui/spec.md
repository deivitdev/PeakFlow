## ADDED Requirements

### Requirement: Cycling-Specific Layout
The system SHALL provide a layout optimized for cycling that prioritizes power (W), cadence (RPM), and speed (KM/H) alongside distance and elevation.

#### Scenario: View cycling activity
- **WHEN** the user opens a cycling activity
- **THEN** the system SHALL display a layout with prominent power and cadence metrics and synchronized telemetry charts.

### Requirement: Strength-Specific Layout
The system SHALL provide a layout optimized for strength training that prioritizes heart rate zones, calories, and duration, omitting distance or speed metrics if they are zero or irrelevant.

#### Scenario: View strength activity
- **WHEN** the user opens a strength training activity
- **THEN** the system SHALL replace the route map with an abstract visual backdrop and highlight time spent in heart rate zones.

### Requirement: Walking-Specific Layout
The system SHALL provide a layout optimized for walking that highlights distance and route exploration.

#### Scenario: View walking activity
- **WHEN** the user opens a walking activity
- **THEN** the system SHALL display a large route map and focus on distance and moving time metrics.

### Requirement: Adaptive HUD Aesthetic
All specialized layouts SHALL adhere to the "Electric Pro" design language, using high-contrast neon accents and glassmorphic containers.

#### Scenario: Aesthetic consistency
- **WHEN** any specialized layout is rendered
- **THEN** it SHALL use the same color palette and typography as the rest of the application.
