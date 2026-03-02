# performance-calculations Specification

## Purpose
TBD - created by archiving change pro-performance-hud. Update Purpose after archive.
## Requirements
### Requirement: Intensity Factor (IF) Calculation
The system SHALL calculate the Intensity Factor (IF) as the ratio of the Normalized Power (Weighted Average Power) to the user's Functional Threshold Power (FTP).

#### Scenario: IF is calculated for a ride with power data
- **WHEN** an activity has `weightedAveragePower` and the user profile has an `ftpWatts` set
- **THEN** the system SHALL calculate `IF = weightedAveragePower / ftpWatts` and display it in the UI.

### Requirement: Training Stress Score (TSS) Calculation
The system SHALL calculate the Training Stress Score (TSS) based on the Intensity Factor (IF), the duration of the activity in seconds, and the user's FTP.

#### Scenario: TSS is calculated for a ride with power data
- **WHEN** an activity has `weightedAveragePower`, duration, and the user profile has an `ftpWatts` set
- **THEN** the system SHALL calculate `TSS = [(duration * weightedAveragePower * IF) / (FTP * 3600)] * 100` and display it in the UI.

