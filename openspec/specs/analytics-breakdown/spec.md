# analytics-breakdown Specification

## Purpose
TBD - created by archiving change activity-type-breakdown. Update Purpose after archive.
## Requirements
### Requirement: Display Activity Type Breakdown
The system SHALL display a breakdown of user activities by type, showing aggregated metrics for each `WorkoutType`. All cycling subtypes (Road, Mountain, Gravel, Generic) SHALL be aggregated and displayed under a single `WorkoutType.CYCLING.GENERIC` category.

#### Scenario: View breakdown for all activities
- **WHEN** the user navigates to the Analytics tab
- **THEN** the system SHALL display a section showing the total count, total distance, and total duration for each distinct `WorkoutType`, with all cycling subtypes consolidated under one "Cycling" entry.

#### Scenario: Visual representation of breakdown
- **WHEN** the activity type breakdown is displayed
- **THEN** the system SHALL provide a clear visual representation (e.g., a bar chart or pie chart) of the proportion of each `WorkoutType` relative to the total, with cycling activities grouped together.

