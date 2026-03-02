## Context

PeakFlow currently displays basic performance metrics (TSS, IF) but doesn't analyze the stability of the aerobic system. Aerobic Decoupling (Pw:Hr) is a standard metric in endurance sports to measure cardiovascular drift.

## Goals / Non-Goals

**Goals:**
- Fetch velocity and power streams from Strava.
- Calculate aerobic decoupling by comparing the first and second halves of an activity.
- Display decoupling percentage and status in activity details.
- Support both Cycling (Power-based) and Walking/Running (Speed-based).

**Non-Goals:**
- Real-time decoupling tracking.
- Complex terrain correction (e.g., GAP - Grade Adjusted Pace) in this phase.

## Decisions

### Data Fetching & Storage
- **Decision**: Update `ActivityStreams` model to include `velocitySmooth` and `watts`.
- **Decision**: Update `StravaApiClient` to request these keys.
- **Decision**: Update `Activity` domain model to hold these series.

### Decoupling Calculation
- **Decision**: Implement calculation in `CalculatePerformanceMetricsUseCase`.
- **Formula**:
    - `EF = Output / HR`
    - `Decoupling = (EF_1 - EF_2) / EF_1`
- **Output selection**:
    - Cycling: Use `watts` stream.
    - Walking/Running: Use `velocity_smooth` stream.
- **Filtering**: Only use data points where the athlete is moving (using `velocity_smooth > 0` or similar).

### UI Representation
- **Decision**: Create a `AerobicEfficiencyCard` component.
- **Location**: In `CyclingDetailContent` and `WalkingDetailContent`, placed after the main bento card.
- **Visuals**: Use a color-coded status (Green for < 5%, Orange/Red for > 5%).

## Risks / Trade-offs

- **[Risk] Data Gaps**: Activities without HR or intensity data cannot have decoupling calculated. → **Mitigation**: Hide the card if data is insufficient.
- **[Risk] Non-Steady Sessions**: Decoupling is only valid for relatively steady efforts. Variable intensity (intervals) will produce noisy decoupling values. → **Mitigation**: Add a small info tooltip explaining that the metric is best for steady-state sessions.
