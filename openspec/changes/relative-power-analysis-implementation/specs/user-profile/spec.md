## MODIFIED Requirements

### Requirement: User Profile Data
The system SHALL allow users to store and manage their personal athlete data, including Name, Weight (kg), Height (cm), and Language Preference. The system SHALL also display a **Performance Summary** derived from this data.

#### Scenario: User saves profile data including language
- **WHEN** the user updates their name, weight, height, and preferred language in the profile screen and clicks "Save"
- **THEN** the system SHALL persist all these data, including the language preference, in local storage

#### Scenario: View performance level in profile
- **WHEN** the user views their profile and has valid weight and FTP data
- **THEN** the system SHALL display their Relative FTP (W/kg) and their performance category (e.g., ADVANCED).
