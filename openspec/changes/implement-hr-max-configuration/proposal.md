# Proposal: Implement HR Max Configuration

## Problem
Heart rate zones (Z1-Z5) are currently calculated using a hardcoded value of 190 BPM. This results in inaccurate metabolic signature and intensity analysis for users whose real HR Max differs from this default.

## Solution
Expose the `hrMaxBpm` field in the Profile screen to allow users to manually set their maximum heart rate. All zone-based calculations will then use this personalized value.

## Scope
- UI update in `ProfileScreen` to add the HR Max field.
- ViewModel updates to persist the value.
- Logic updates in `ActivityRepositoryImpl` and `ActivityDetailViewModel` to respect the user's configuration.
