# Proposal: Android Health Connect Integration

## Problem
PeakFlow relies on manual entry for athlete weight and lacks physiological recovery context (Sleep, HRV). This limits the accuracy of relative metrics (W/kg) and the depth of training recommendations.

## Solution
Integrate Google Health Connect to automatically sync weight, sleep duration, and Heart Rate Variability (HRV). This data will complement Strava activity data to provide a holistic view of the athlete's state.

## Scope
- Implementation of `HealthRepository` for Android.
- UI for permission management.
- Background sync of Weight, Sleep, and HRV.
- Integration of weight into existing W/kg calculations.
