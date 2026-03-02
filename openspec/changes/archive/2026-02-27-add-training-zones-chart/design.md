## Context

The `ActivityDetailScreen` provides time-series charts for heart rate and elevation. To help athletes analyze workout intensity, we need to add a "Training Zones" chart that summarizes the time spent in different physiological heart rate zones (Z1-Z5).

## Goals / Non-Goals

**Goals:**
- Implement a `ZonesDistributionChart` component.
- Calculate time-in-zone percentages from the existing `heartRateSeries`.
- Adhere to the "Electric Pro" high-contrast HUD aesthetic.
- Display both percentage and absolute time per zone.

**Non-Goals:**
- Customizing zone thresholds (fixed for now based on standard percentages).
- Zones for other metrics like power or pace.

## Decisions

### 1. Component UI: Horizontal Bar List
- **Rationale**: A list of horizontal bars is more readable on mobile screens and allows for clear labeling of each zone's name and duration.
- **Style**: Each bar will have a color corresponding to the intensity level (e.g., Cool Blue for Z1, Fiery Red for Z5).

### 2. Data Processing: Bucket Calculation
- **Rationale**: The calculation should happen in the `ActivityDetailViewModel` to keep the UI component focused on rendering.
- **Logic**: Iterate through `heartRateSeries` and count occurrences in each predefined range.

### 3. Localization: Key-Value Strings
- **Rationale**: Use `strings.xml` for zone labels ("Recovery", "Endurance", etc.) to ensure English and Spanish support.

## Risks / Trade-offs

- **[Risk] Accuracy** → **Mitigation**: Use a standard maximum heart rate formula (220 - age) if the user's max HR isn't in the profile, or a reasonable default (e.g., 190 BPM).
- **[Risk] Visual Noise** → **Mitigation**: Place the zones chart in its own Bento card to separate it from the time-series telemetry.
