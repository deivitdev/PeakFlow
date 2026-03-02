## Context

Running is a high-impact, pace-sensitive sport that requires a specialized data presentation. Strava provide rich data for runs, including kilometer splits and cadence, which are not currently utilized in the app.

## Goals / Non-Goals

**Goals:**
- Add `RUNNING` to `WorkoutType`.
- Map Strava `Run` and `TrailRun` to `RUNNING`.
- Create a `RunningDetailContent` component.
- Display Pace (min/km) instead of Speed (km/h).
- Visualize kilometer splits with "fastest/slowest" indicators.
- Calculate and display estimated sweat loss.

**Non-Goals:**
- Real-time GPS tracking.
- Audio cues or training plans.

## Decisions

### Workout Type Mapping
- **Decision**: Update `WorkoutType` sealed class to include `RUNNING` (Road, Trail, Treadmill).
- **Rationale**: Categorization allows for better UI specialization.

### Metric Selection: Pace over Speed
- **Decision**: All primary running metrics will use Pace (min/km).
- **Rationale**: Runners measure performance by time-per-distance, not distance-per-time.

### Split Highlighting
- **Decision**: In the split list, the fastest split will be highlighted in green/neon, and the slowest in orange/red.
- **Rationale**: Immediate visual feedback on where the athlete was strongest.

### Sweat Loss Algorithm
- **Decision**: Use a basic heuristic: `Duration (hours) * Intensity (IF) * 0.8 liters`.
- **Rationale**: Provides a useful baseline for hydration without needing physiological sensors.

## Risks / Trade-offs

- **[Risk] Missing Cadence**: Some users run without sensors. → **Mitigation**: Hide the cadence metric if the stream is empty.
- **[Risk] Split Accuracy**: Treadmill runs might have inaccurate GPS splits. → **Mitigation**: Default to Strava's reported splits if available.
