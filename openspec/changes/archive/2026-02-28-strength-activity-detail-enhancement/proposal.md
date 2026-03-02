## Why

The current Strength activity detail screen is minimal and doesn't leverage all the data Strava provides. Strength training users value understanding their intensity (Relative Effort), heart rate peaks, and rest efficiency (Moving vs. Elapsed time). Enhancing this screen aligns with our "Electric Pro" aesthetic and provides more value to gym-goers.

## What Changes

- **Intensity Card (Relative Effort)**: Implement a high-visibility card for Strava's `suffer_score`, including a textual intensity level (e.g., "Moderate", "Hard").
- **Time Analysis Section**: Display both `Moving Time` and `Elapsed Time` side-by-side to highlight rest periods during the session.
- **Enhanced Heart Rate Metrics**: Add `Max Heart Rate` alongside the average, and visualize the heart rate peaks more clearly.
- **Improved Abstract Backdrop**: Refine the abstract visualization for strength activities to be more dynamic based on session intensity.

## Capabilities

### New Capabilities
- None.

### Modified Capabilities
- `specialized-activity-ui`: Update requirements for the Strength-specific layout to include intensity and time analysis.

## Impact

- **UI**: New components in `StrengthDetailContent.kt`.
- **Localization**: New strings for intensity levels and labels.
- **User Experience**: Richer feedback for non-cardio activities.
