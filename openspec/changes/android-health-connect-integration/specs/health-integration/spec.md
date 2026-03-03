# Spec: Health Data Integration

## Functional Requirements
- **FR1: Availability Check**: The system MUST detect if Health Connect is available on the Android device.
- **FR2: Permission Management**: The system SHALL provide a UI to request `READ` access for Weight, Sleep, and HRV.
- **FR3: Automated Weight Sync**: The system SHALL update the global `UserProfile.weightKg` whenever a newer weight record is found.
- **FR4: Historical Sync**: On the first successful connection, the system SHOULD attempt to sync the last 30 days of health data.
- **FR5: Contextual Education**: Every health metric card MUST include a tooltip or info icon that explains the metric's meaning and its impact on performance.

## Scenarios
### Scenario: User requests metric info
When the user taps the info icon next to "HRV", the app SHALL display a tooltip explaining that RMSSD is a measure of autonomic nervous system balance and how it relates to recovery.
