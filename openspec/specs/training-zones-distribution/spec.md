# training-zones-distribution Specification

## Purpose
TBD - created by archiving change add-training-zones-chart. Update Purpose after archive.
## Requirements
### Requirement: Training Zone Calculation
The system SHALL categorize heart rate data points into five zones (Z1-Z5) based on standard percentage thresholds of maximum heart rate (or user-defined thresholds if available).

#### Scenario: Heart rate point categorization
- **WHEN** an activity with heart rate data is processed
- **THEN** the system SHALL calculate the percentage of time spent in each zone:
    - Z1 (Recovery): < 60%
    - Z2 (Endurance): 60-70%
    - Z3 (Aerobic): 70-80%
    - Z4 (Threshold): 80-90%
    - Z5 (Anaerobic): > 90%

### Requirement: Zone Distribution Chart
The system SHALL display a horizontal or vertical bar chart showing the time spent in each of the five heart rate zones.

#### Scenario: Displaying the chart
- **WHEN** the user opens the activity detail screen for an activity with heart rate data
- **THEN** the system SHALL render a chart with colored bars representing each zone (e.g., Gray, Blue, Green, Orange, Red) and labels for Z1-Z5.

### Requirement: Multi-language Support
The labels and descriptions for training zones SHALL be localized into English and Spanish.

#### Scenario: Spanish localization
- **WHEN** the user language is set to Spanish
- **THEN** the chart labels SHALL use Spanish terms (e.g., "Zonas de Entrenamiento").

