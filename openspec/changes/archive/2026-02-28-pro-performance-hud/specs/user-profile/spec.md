## MODIFIED Requirements

### Requirement: User Profile Data
The system SHALL allow users to store and manage their personal athlete data, including Name, Weight (kg), Height (cm), and Language Preference.

#### Scenario: User saves profile data including language
- **WHEN** the user updates their name, weight, height, and preferred language in the profile screen and clicks "Save"
- **THEN** the system SHALL persist all these data, including the language preference, in local storage

## ADDED Requirements

### Requirement: Athlete Performance Data
The system SHALL allow users to store and manage their athletic performance data, specifically their Functional Threshold Power (FTP) in watts.

#### Scenario: User saves their FTP
- **WHEN** the user enters their FTP value in the profile screen and clicks "Save"
- **THEN** the system SHALL persist the `ftpWatts` value in local storage.
