## MODIFIED Requirements

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
