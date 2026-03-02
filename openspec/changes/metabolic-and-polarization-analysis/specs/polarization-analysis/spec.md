## ADDED Requirements

### Requirement: 80/20 Polarization Analysis (7 and 28 Days)
The system SHALL aggregate the total time spent in intensity zones for all activities over 7 and 28-day windows to calculate the intensity distribution ratio.

#### Scenario: Weekly polarization ratio calculation
- **WHEN** the user views the analytics dashboard
- **THEN** the system SHALL display a chart showing the percentage of total time spent in each zone category (Low, Threshold, High).

### Requirement: "Grey Zone" (Z3) Overtraining Detection
The system SHALL monitor the percentage of time spent in Zone 3 (Tempo/Threshold) relative to total training time.

#### Scenario: Alert for excessive Zone 3 training
- **WHEN** the 28-day analysis shows that more than 40% of training time has been spent in Zone 3
- **THEN** the system SHALL provide a "Grey Zone Alert" suggesting a shift towards more polarized training (Lower or Higher intensity).

### Requirement: Polarization Trend Visualization
The system SHALL visualize the historical trend of the polarization ratio to allow users to monitor their training discipline over time.

#### Scenario: View monthly polarization trend
- **WHEN** the user opens the monthly performance summary
- **THEN** the system SHALL render a trend line showing the week-over-week change in the 80/20 distribution.
