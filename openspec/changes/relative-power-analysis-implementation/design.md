## Context

Currently, the application stores user weight and FTP but doesn't relate them to performance analysis. By implementing W/kg, we align with professional training standards and make the profile data actionable.

## Goals / Non-Goals

**Goals:**
- Calculate Relative FTP (W/kg) based on profile data.
- Classify users into performance categories (Recreational to Pro).
- Display Relative Power (Avg/Max W/kg) in Cycling activity details.
- Provide improvement targets (required Watts or Weight loss).

**Non-Goals:**
- Automatic adjustment of Strava zones.
- Support for W/kg in sports other than Cycling (e.g., Running Power) in this phase.

## Decisions

### Domain Logic Location
- **Decision**: Implement a `CalculateRelativePowerUseCase`.
- **Rationale**: This logic will be used by both the `ProfileViewModel` (for overall level) and `ActivityDetailViewModel` (for specific session stats). Centralizing it in a use case ensures consistency.

### Categorization Scale (Coggan Scale)
- **Decision**: Use the following W/kg thresholds for classification:
    - Recreational: < 2.0
    - Intermediate: 2.0 - 2.99
    - Advanced: 3.0 - 3.99
    - Elite: 4.0 - 4.99
    - World Class: >= 5.0
- **Rationale**: These are simplified versions of industry-standard coaching scales.

### UI Integration
- **Decision**: Add a `PerformanceLevelCard` to the Profile screen.
- **Decision**: Update `CyclingDetailContent` to show `W/kg` as a sub-metric under Power.
- **Rationale**: The profile is the logical place for static level analysis, while activity detail is best for dynamic session analysis.

## Risks / Trade-offs

- **[Risk] Weight Fluctuations**: If a user's weight changes significantly but they don't update their profile, historic W/kg calculations for recent activities will be incorrect. → **Mitigation**: Future iterations could store weight-at-time-of-activity, but for now, we will use the current profile weight.
- **[Risk] Privacy**: W/kg explicitly relates weight to power. → **Mitigation**: This data remains local to the device and is not shared.
