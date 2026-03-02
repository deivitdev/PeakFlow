## MODIFIED Requirements

### Requirement: Cycling-Specific Layout
The system SHALL provide a layout optimized for cycling that prioritizes power (W), cadence (RPM), speed (KM/H), and **Aerobic Efficiency (Decoupling)**.

#### Scenario: View cycling activity
- **WHEN** the user opens a cycling activity
- **THEN** the system SHALL display a layout with prominent power, cadence metrics, and an **Aerobic Efficiency card** if HR and power data are available.

### Requirement: Walking-Specific Layout
The system SHALL provide a layout optimized for walking that highlights distance, pace, environment, gear, and **Aerobic Efficiency (Decoupling)**.

#### Scenario: View walking activity
- **WHEN** the user opens a walking activity
- **THEN** the system SHALL display a large route map and an **Aerobic Efficiency card** if HR and pace data are available.
