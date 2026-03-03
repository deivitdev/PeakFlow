# Proposal: Optimize Metabolic Signature Source

## Problem
The Metabolic Signature card and Hydration recommendations currently only use the absolute last activity in chronological order. If the last activity is a manual entry, a short walk without a heart rate sensor, or a session with 0 calories, the analytics show "0 / 0", which is frustrating for the user and hides previous valuable data.

## Solution
Update the analysis engine to perform a "deep search" for the most recent **valid** activity. An activity is considered valid for metabolic analysis if it contains:
- Calories > 0
- Heart Rate data or Power data (resulting in time spent in zones Z1-Z5).

## Scope
- Modify `GetAdvancedPerformanceInsightsUseCase` to iterate through the last 10 activities to find a valid source.
- Update `CalculateMetabolicOxidationUseCase` to return a "validity" flag or handle empty data gracefully.
- Ensure the search stops as soon as a valid activity is found to minimize processing.
