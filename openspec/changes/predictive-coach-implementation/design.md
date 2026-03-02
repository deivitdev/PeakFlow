## Context

The current PeakFlow architecture calculates Fitness (CTL), Fatigue (ATL), and Form (TSB) based on historical Training Stress Score (TSS) data. While effective for review, it doesn't help the user decide what to do today. By solving the CTL/ATL equations for a target TSB, we can provide specific TSS recommendations.

## Goals / Non-Goals

**Goals:**
- Implement an inverse-solver for the Fitness/Fatigue model.
- Create a multi-sport balance auditor (focusing on the rolling 7-day window).
- Integrate predictions into the Insights tab UI.
- Visualize the impact of these predictions on the existing charts.

**Non-Goals:**
- Creating a fixed "Training Plan" or calendar (suggestions are daily and dynamic).
- Automatic workout generation (no specific intervals or exercises, just load targets).

## Decisions

### 1. The Inverse TSB Solver
To suggest a TSS for tomorrow's target TSB, we use the following derived logic:
- `CTL_tomorrow = CTL_today + (TSS_today - CTL_today) / 42`
- `ATL_tomorrow = ATL_today + (TSS_today - ATL_today) / 7`
- `TSB_tomorrow = CTL_today - ATL_today` (Note: TSB is usually calculated from the previous day's state).
- **Target Calculation**: We will define three target TSB zones for "tomorrow" and solve for `TSS_today` that lands the athlete in that zone.

### 2. Sport Balance Heuristics
We will implement a "Negligence Detector" that scans the last 7 days of activities.
- **Rules**:
    - If `STRENGTH` sessions < 1 in 7 days → Priority: Strength.
    - If `CYCLING` or `RUNNING` (primary sport) has > 4 sessions and 0 cross-training → Suggest "Active Recovery / Mobility".

### 3. UI: The "Daily Path Selector"
- **Decision**: Use a horizontal pager or a set of three cards at the top of the Insights tab.
- **Interactivity**: Clicking a card will "preview" the line on the Fitness/Fatigue chart as a dashed projection.

### 4. Nutriton Forecast (Glycogen)
- **Decision**: Use the `CalculateMetabolicOxidationUseCase` piecewise linear model assuming a standard intensity factor (IF) distribution for the suggested TSS.
    - High TSS (~100+) assumes higher Z4/Z5 contribution.
    - Low TSS (~30) assumes Z1/Z2 contribution.

## Risks / Trade-offs

- **[Risk] Mathematical edge cases** → If an athlete is extremely fatigued, the "Build" path might suggest an impossibly high TSS.
    - **Mitigation**: Cap suggestions at `2.5 * CTL` or a hard limit of 300 TSS.
- **[Risk] Chart complexity** → Adding dashed lines might make the chart hard to read.
    - **Mitigation**: Only show the projection for the *currently selected* path card.
