## Why

Currently, the application provides descriptive metrics of past activities but lacks actionable insights regarding the user's overall physical condition and training load. Implementing the Fitness vs. Fatigue model (CTL/ATL/TSB) will transform the app into a proactive training partner, helping users optimize their efforts, identify peak performance windows, and significantly reduce the risk of overtraining and injury.

## What Changes

- **Core Analytics Engine**: Implementation of the exponentially weighted moving average (EWMA) algorithm to calculate CTL (Chronic Training Load) and ATL (Acute Training Load).
- **Form Calculation**: Dynamic calculation of TSB (Training Stress Balance) to determine the user's "Form" or readiness.
- **Training Status UI**: A new visual indicator on the Analytics dashboard that interprets TSB into human-readable statuses (e.g., "FRESH", "OPTIMAL", "OVERREACHING").
- **Performance Trends Chart**: A multi-line visualization showing the interaction between Fitness, Fatigue, and Form over time.

## Capabilities

### New Capabilities
- `fitness-fatigue-model`: Science-based tracking of training load and recovery readiness.

### Modified Capabilities
- `performance-analytics`: Integration of the new fitness-fatigue metrics and visualizations into the main analytics dashboard.

## Impact

- **Domain Layer**: New use cases for load calculations.
- **Data Layer**: Potential need for caching or indexing 42-day activity windows for efficient calculation.
- **Presentation Layer**: Enhancements to the `AnalyticsScreen` and `AnalyticsViewModel`.
- **User Experience**: Higher-level interpretation of activity data for better training decisions.
