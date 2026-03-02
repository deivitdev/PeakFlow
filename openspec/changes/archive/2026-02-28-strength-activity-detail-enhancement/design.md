## Context

The current strength activity detail view lacks depth. We have access to `sufferScore`, `elapsedTimeSeconds`, and `maxHeartRate` from Strava, which are not currently displayed.

## Goals / Non-Goals

**Goals:**
- Implement an "Intensity" card showing the Relative Effort (suffer score).
- Compare Moving vs. Elapsed time to show session efficiency.
- Display Max HR and Avg HR together.
- Maintain the "Electric Pro" design language.

**Non-Goals:**
- Changing the heart rate zones visualization (already implemented).
- Implementing new data fetching logic (data is already in the domain model).

## Decisions

### Relative Effort (Suffer Score) Visualization
- **Decision**: Create a `RelativeEffortCard` that uses a color-coded label based on the score.
- **Rationale**: The score alone is abstract; textual context (Easy, Moderate, Hard) makes it actionable.
- **Thresholds**: 
  - < 15: EASY (Green/Blue)
  - 15-30: MODERATE (Yellow)
  - 31-60: HARD (Orange)
  - \> 60: EXTREME (Red)

### Time Analysis
- **Decision**: Use a side-by-side comparison of Moving Time and Elapsed Time.
- **Rationale**: In strength training, the difference represents rest time, which is a key performance metric.

### UI Structure
- **Decision**: Update `StrengthDetailContent.kt` to include these new sections before the heart rate zones.
- **Rationale**: Grouping athlete data first follows the established hierarchy.

## Risks / Trade-offs

- **[Risk] Missing Data**: Not all Strava activities have a suffer score (requires HR data).
- **[Mitigation]**: Hide the intensity card if `sufferScore` is null.
