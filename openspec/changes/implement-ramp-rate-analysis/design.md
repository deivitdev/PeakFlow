## Context

The current Performance Analysis dashboard in PeakFlow provides a snapshot of current fitness (CTL), fatigue (ATL), and form (TSB). However, it lacks a delta metric to show how quickly these values are changing. The Ramp Rate is a standard industry metric used to monitor the rate of fitness acquisition, acting as a crucial safety net against injury.

## Goals / Non-Goals

**Goals:**
- Implement the mathematical logic to compute the Ramp Rate from historical CTL data.
- Update the domain models to transport Ramp Rate information to the UI.
- Create a visual indicator (gauge or status text) in the Fitness & Fatigue section.
- Provide localized safety classifications and descriptions.

**Non-Goals:**
- Creating a separate page for Ramp Rate (it will be integrated into the existing dashboard).
- Predictive modeling of future Ramp Rate (current focus is on the actual weekly increase).

## Decisions

### 1. Calculation Point
- **Decision**: Calculate the Ramp Rate within the `GetFitnessFatigueUseCase`.
- **Rationale**: Since this UseCase already generates the full history of CTL points to render the chart, it has immediate access to the value from 7 days ago without extra database queries.

### 2. Status Classification Logic
- **Decision**: Use a simple enum-based classification:
    - `MAINTAINING`: < 2 pts/week
    - `PRODUCTIVE`: 2 - 5 pts/week
    - `INTENSE`: 5 - 8 pts/week
    - `RISKY`: > 8 pts/week
- **Rationale**: These thresholds are widely accepted in endurance training literature (e.g., TrainingPeaks) and provide clear, actionable feedback.

### 3. UI Integration
- **Decision**: Add a new row in the `TrainingStatusCard` or a dedicated small card next to the TSB value.
- **Rationale**: This keeps the most critical physiological status metrics (Form and Progression Speed) grouped together.

## Risks / Trade-offs

- **[Risk] Data Gaps** → If the user has fewer than 7 days of data, the Ramp Rate cannot be calculated.
    - **Mitigation**: Return a `null` or "Insufficient Data" state if the history is shorter than 7 points.
- **[Risk] UI Overload** → Adding more numbers might clutter the card.
    - **Mitigation**: Use a visual "speedometer" style bar or color-coded chips to make it scannable without reading every number.
