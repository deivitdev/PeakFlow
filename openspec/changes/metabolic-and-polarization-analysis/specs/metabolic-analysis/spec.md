## ADDED Requirements

### Requirement: Metabolic Substrate Oxidation Estimation
The system SHALL estimate the relative contribution of Fats and Carbohydrates to total energy expenditure based on the percentage of the user's maximum heart rate (%HRmax) or Functional Threshold Power (%FTP).

#### Scenario: Estimate glycogen consumption
- **WHEN** an activity is completed and intensity distribution is analyzed
- **THEN** the system SHALL display the estimated grams of carbohydrates (glycogen) consumed during the session.

### Requirement: Hydration and Electrolyte Recommendation
The system SHALL provide a post-activity hydration recommendation based on the activity's duration, Intensity Factor (IF), and the average ambient temperature.

#### Scenario: High-heat hydration alert
- **WHEN** an activity's average temperature is above 25°C and the Intensity Factor is above 0.85
- **THEN** the system SHALL display a specific hydration alert recommending water and electrolyte replenishment.

### Requirement: Glycogen Recovery Window Estimation
The system SHALL estimate the time required for full glycogen replenishment based on the estimated carbohydrate oxidation.

#### Scenario: Suggest recovery duration
- **WHEN** an activity is estimated to have depleted more than 70% of estimated glycogen stores
- **THEN** the system SHALL suggest a recovery window of 24 to 48 hours before the next high-intensity effort.
