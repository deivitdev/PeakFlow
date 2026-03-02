## Context

The application currently has a solid foundation for tracking individual activities and basic trends. We have `TSS` (Training Stress Score) calculated for activities with power data. To implement the CTL/ATL/TSB model, we need to extend this calculation to all activities (using heart rate or perceived exertion as fallbacks) and implement the time-series aggregation logic.

## Goals / Non-Goals

**Goals:**
- Implement the exponentially weighted moving average (EWMA) algorithm for CTL and ATL.
- Provide a clear "Training Status" summary on the Analytics dashboard.
- Create a visualization that shows the interplay between Fitness, Fatigue, and Form.
- Ensure calculations are performant even with a large history of activities.

**Non-Goals:**
- Real-time physiological modeling (e.g., HRV integration) in this phase.
- Automatic adjustment of user goals based on the model (only recommendations).

## Decisions

### Calculation Logic: EWMA
- **Decision**: Use the standard exponentially weighted moving average formula.
    - `CTL_today = CTL_yesterday + (TSS_today - CTL_yesterday) / 42`
    - `ATL_today = ATL_yesterday + (TSS_today - ATL_yesterday) / 7`
- **Rationale**: This is the industry-standard implementation used by TrainingPeaks and Strava, providing a smooth trend that accurately reflects physiological adaptation and recovery.

### Fallback TSS Calculation
- **Decision**: For activities without power data, estimate TSS based on `sufferScore` (Heart Rate) or duration/intensity.
- **Rationale**: The model requires a TSS value for every day to be accurate. We will use a mapping like `EstimatedTSS = sufferScore * multiplier`.

### Data Storage & Performance
- **Decision**: Pre-calculate daily TSS aggregates in the database or a specialized cache.
- **Rationale**: Calculating a 42-day window on every UI refresh could be slow if we have hundreds of activities. We will create a `DailyLoad` table to store aggregated TSS.

### Visualization: Multi-Line Canvas Chart
- **Decision**: Extend the custom `Canvas` charting logic to support multiple series (CTL, ATL, TSB) with different colors and a shared X-axis.
- **Rationale**: Standard charting libraries are often too heavy for KMP or don't provide the "Electric Pro" look we want.

## Risks / Trade-offs

- **[Risk] Cold Start Data**: Users with less than 42 days of data will have inaccurate CTL values.
- **[Mitigation]**: Show a "Building Data" status until enough history is available, or allow users to "seed" their initial fitness level.
- **[Risk] Database Complexity**: Adding more tables/migrations increases maintenance.
- **[Mitigation]**: Keep the `DailyLoad` table simple and derive it from existing `Activity` data during sync.
