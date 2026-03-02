## Why

The current Walking activity detail screen is missing key metrics that Strava provides, such as pace analysis, temperature, and gear tracking (shoes). Walking users value seeing their pace (min/km) rather than just speed (km/h), and understanding how environment (temp) and elevation extremes affected their session.

## What Changes

- **Pace Analysis**: Add a prominent "Average Pace" and "Max Pace" metric, calculated from speed.
- **Enhanced Bento Card**: Update the main metrics card to include:
    - Pace (min/km) instead of Speed (km/h) as it's more idiomatic for walking.
    - Max Pace.
    - Elapsed Time vs Moving Time analysis (rest detection).
- **Environment & Elevation Detail**: New section for Average Temperature and Elevation extremes (Max/Min elevation).
- **Gear Integration**: Display the gear (shoes) used for the walk and its total accumulated distance.
- **Pace Formatter**: Implement a utility to convert speed (km/h) to pace (min/km).

## Capabilities

### New Capabilities
- None.

### Modified Capabilities
- `specialized-activity-ui`: Update the walking-specific layout requirements to include pace, temperature, and gear.

## Impact

- **UI**: Significant updates to `WalkingDetailContent.kt`.
- **Utils**: New pace formatting logic in `Formatters.kt`.
- **Localization**: New strings for pace labels, temperature, and gear.
- **Consistency**: Brings the walking screen to the same level of detail as cycling and strength.
