# Tasks: Optimize Metabolic Signature Source

## Phase 1: Logic Implementation
- [x] Add `totalIntensitySeconds` helper to the `Activity` domain model or repository to easily check for valid zone data.
- [x] Refactor `GetAdvancedPerformanceInsightsUseCase` to iterate through the last 10 activities to find the first valid source.
- [x] Ensure `hydrationAmountLiters` calculation also uses the newly found `validActivity`.

## Phase 2: Verification
- [ ] Test with a scenario where the last activity is empty (0 calories) but the second-to-last is valid.
- [ ] Verify that the search limit (10) prevents infinite loops or excessive API calls.
- [ ] Confirm the UI updates correctly in the Analytics screen.
