## MODIFIED Requirements

### Requirement: Detailed Data Display
The system SHALL display specialized layouts based on activity type, where map data is integrated as a Bento card that can be expanded into an immersive view. The system SHALL further specialize the layout based on Strava's `sport_type` (e.g., RoadRide, MountainBikeRide, GravelRide) to prioritize specific "Hero" metrics like Elevation for MTB or Power for Road.

#### Scenario: Unified layout with map portal
- **WHEN** an outdoor activity is viewed
- **THEN** the system SHALL present the data grid first, including a map thumbnail, rather than having the map as a global background layer.

#### Scenario: Specialized Hero metrics for MTB
- **WHEN** a `MountainBikeRide` is viewed
- **THEN** the system SHALL prioritize elevation gain and extreme points (`elev_high`, `elev_low`) in the primary bento cards.

## ADDED Requirements

### Requirement: Gear HUD Slot
The system SHALL display a dedicated HUD card showing the bike or gear used during the activity, including its total accumulated mileage from Strava.

#### Scenario: View bike mileage
- **WHEN** an activity is viewed and gear information is available from Strava
- **THEN** the system SHALL display the gear name and its total lifetime distance in the activity detail.

### Requirement: Performance Telemetry Expansion
The system SHALL include expanded performance metrics such as `average_temp`, `elev_high`, `elev_low`, `max_watts`, `kilojoules`, and the recording `device_name`.

#### Scenario: View telemetry extremes
- **WHEN** the user views the activity detail
- **THEN** the system SHALL display the max elevation point and the peak power achieved.
