# user-profile Specification

## Purpose
TBD - created by archiving change add-user-profile-tab. Update Purpose after archive.
## Requirements
### Requirement: User Profile Data
The system SHALL allow users to store and manage their personal athlete data, including Name, Weight (kg), Height (cm), and Language Preference.

#### Scenario: User saves profile data including language
- **WHEN** the user updates their name, weight, height, and preferred language in the profile screen and clicks "Save"
- **THEN** the system SHALL persist all these data, including the language preference, in local storage

### Requirement: Profile Tab Accessibility
The system SHALL provide a dedicated tab in the bottom navigation bar to access the user profile.

#### Scenario: User navigates to profile
- **WHEN** the user clicks the "Profile" icon in the navigation bar
- **THEN** the system SHALL display the Profile screen

### Requirement: Athlete Performance Data
The system SHALL allow users to store and manage their athletic performance data, specifically their Functional Threshold Power (FTP) in watts.

#### Scenario: User saves their FTP
- **WHEN** the user enters their FTP value in the profile screen and clicks "Save"
- **THEN** the system SHALL persist the `ftpWatts` value in local storage.

