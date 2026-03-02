# morphing-transitions Specification

## Purpose
TBD - created by archiving change morphing-map-portal. Update Purpose after archive.
## Requirements
### Requirement: Integrated Map Thumbnail
The system SHALL display a compact map thumbnail within the activity metrics grid for activities that contain geographic data.

#### Scenario: View map thumbnail
- **WHEN** the user opens an activity with a route
- **THEN** the system SHALL show a non-interactive mini-map card alongside other metrics.

### Requirement: Morphing Transition to Full Screen
The system SHALL implement a smooth, context-preserving animation when expanding the map thumbnail to full screen.

#### Scenario: Expand map
- **WHEN** the user taps the map thumbnail
- **THEN** the map SHALL animate from its original card bounds to cover the entire screen area.

### Requirement: Full Screen Map Interactivity
Once expanded, the map SHALL allow full user interaction, including panning, zooming, and rotation.

#### Scenario: Interact with expanded map
- **WHEN** the map is in full-screen mode
- **THEN** the system SHALL enable all standard map gestures and display a prominent "Close" button.

