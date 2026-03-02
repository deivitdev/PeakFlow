## Context

The PeakFlow application currently processes individual activity telemetry (heart rate, power) to calculate basic performance metrics (TSS, IF). However, it lacks a higher-level analysis engine that interprets these metrics for strategic training (Polarization) or physiological insights (Metabolic oxidation). The current data model supports storing time-in-zones and environmental data (temperature), providing the necessary foundation for these advanced features.

## Goals / Non-Goals

**Goals:**
- Implement a metabolic estimation model to calculate Carbohydrate vs. Fat usage based on intensity.
- Implement a polarization engine that aggregates training intensity distribution over 7 and 28-day windows.
- Provide actionable alerts for inefficient "Grey Zone" training.
- Generate proactive hydration recommendations based on metabolic heat and environment.
- Integrate these new insights into the Analytics dashboard with intuitive visualizations.

**Non-Goals:**
- Real-time metabolic tracking during an active workout (post-activity analysis only).
- Manual logging of food or water intake.
- Integration with external glucose or core temperature sensors.

## Decisions

### 1. Metabolic Piecewise Linear Model
We will implement a model that maps the percentage of intensity (relative to FTP or HRmax) to substrate oxidation rates.
- **Decision**: Use the following zones:
    - Z1-Z2: 70% Fat / 30% Carbs
    - Z3: 50% Fat / 50% Carbs
    - Z4: 20% Fat / 80% Carbs
    - Z5: 5% Fat / 95% Carbs
- **Rationale**: This is a widely accepted physiological approximation in endurance sports literature (e.g., San Millán et al.) that provides significant educational value without requiring laboratory metabolic testing.

### 2. Time-Windowed Polarization Aggregation
- **Decision**: The `ActivityRepository` will be expanded to support querying aggregated time-in-zones for specific date ranges (Last 7 Days, Last 28 Days).
- **Rationale**: This allows the `AnalyticsViewModel` to calculate the 80/20 ratio efficiently without fetching all raw activities into memory.

### 3. Integrated Analytics Dashboard
- **Decision**: Rather than creating a separate "Advanced Metrics" screen, we will extend the `AnalyticsScreen` with new "Bento-style" cards for:
    - **Polarization Gauge**: Showing the Low/Mid/High distribution.
    - **Metabolic Signature**: A split bar showing Carb vs. Fat usage for the most recent week.
    - **Proactive Alerts**: A section for "Grey Zone" and "Hydration" recommendations.
- **Rationale**: Maintains a clean UX and encourages users to visit the Analytics tab as their primary source of truth.

## Risks / Trade-offs

- **[Risk] Accuracy of Profile Data** → These metrics are highly dependent on accurate FTP and HRmax settings.
    - **Mitigation**: Display a small info icon or tooltip in the Analytics screen explaining that metrics are estimates based on the User Profile.
- **[Risk] Missing Environmental Data** → Not all Strava activities include temperature.
    - **Mitigation**: The hydration recommendation will only be triggered if `averageTemp` is present in the activity data. If missing, the recommendation will be omitted.
- **[Risk] Performance of Aggregated Queries** → Calculating sums across many activities could be slow on old devices.
    - **Mitigation**: Use SQLDelight's optimized aggregation functions (SUM) rather than mapping to domain objects before summing.
