## Context

The walking activity screen currently displays basic metrics (distance, moving time, elevation gain, calories) but lacks the depth of other specialized screens. We have access to more environmental and performance data from Strava that can be surfaced to provide a more comprehensive view of a walk.

## Goals / Non-Goals

**Goals:**
- Implement pace-based analysis (min/km) which is the primary metric for walking/running.
- Show environmental data (temperature) and elevation extremes.
- Integrate gear tracking (shoes).
- Analyze rest periods (Moving vs Elapsed time).

**Non-Goals:**
- Implementing real-time tracking (the app is for analysis of synced activities).
- Changing the telemetry charts (they already support heart rate and elevation).

## Decisions

### Pace Calculation
- **Decision**: Add a `calculatePace(speedKmh: Float)` helper in `Formatters.kt`.
- **Logic**: Pace (min/km) = 60 / speedKmh. Format as "MM:SS /km".
- **Rationale**: Speed in km/h is less intuitive for walkers than pace.

### UI Structure
- **Decision**: Update `WalkingDetailContent.kt` to include:
    - **Hero Card**: Distance, Moving Time, Average Pace, Max Pace.
    - **Environment Section**: Temperature, Max/Min Elevation, Rest Time.
    - **Gear Section**: Using an enhanced `GearCard` with a walking icon.
- **Rationale**: Grouping metrics by theme (Performance, Environment, Gear) improves scanability.

### GearCard Enhancement
- **Decision**: Modify `GearCard.kt` to accept an `ImageVector` for the icon.
- **Rationale**: Allows using a bike icon for cycling and a walk icon for walking activities.

## Risks / Trade-offs

- **[Risk] Division by Zero**: Pace calculation if speed is 0. → **Mitigation**: Handle 0 speed gracefully by returning "--:--" or a very high pace.
- **[Risk] Missing Temperature**: Strava doesn't always provide temperature. → **Mitigation**: Only show the environment section if at least one metric (temp or elevation extremes) is available.
