## Why

Running is one of the most common activities on Strava, but it requires a significantly different analysis approach compared to cycling or strength training. Currently, the app lacks a dedicated view for runners, missing out on crucial metrics like pace (min/km), split-by-split analysis, and running cadence. Providing a specialized running dashboard will attract a major user segment and deliver high-value insights into technique and efficiency.

## What Changes

- **Running Data Mapping**: Map Strava `Run` and `TrailRun` activity types to a new internal `RUNNING` workout type.
- **Dedicated Running Detail Screen**: A new UI layout specifically for running activities.
- **Pace-Centric Metrics**: Display average and max pace in min/km as primary metrics.
- **Running Cadence Support**: Display steps per minute (spm) if cadence data is available.
- **Advanced Split Visualization**: A detailed list of kilometer splits with performance highlighting.
- **Sweat & Hydration Estimation**: Implementation of a basic sweat loss estimator based on intensity and duration.

## Capabilities

### New Capabilities
- None.

### Modified Capabilities
- `running-specialized-ui`: Implement the full requirements for the specialized running interface.
- `specialized-activity-ui`: Update general specialized UI requirements to include running as a first-class citizen.

## Impact

- **Domain Model**: Update `WorkoutType` enum and `Activity` model.
- **Data Layer**: Update mappers to handle running types and cadence streams.
- **Presentation Layer**: New `RunningDetailContent.kt` and updates to `ActivityDetailScreen.kt`.
- **Localization**: New strings for running-specific labels and units.
