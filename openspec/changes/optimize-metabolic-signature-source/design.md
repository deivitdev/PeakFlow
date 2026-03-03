# Design: Optimize Metabolic Signature Source

## Core Logic Update
The `GetAdvancedPerformanceInsightsUseCase` will be refactored to replace:
```kotlin
val lastActivity = activities.firstOrNull()?.let { activityRepository.getActivity(it.id) }
```
with an iterative search:
```kotlin
val validActivity = activities.take(10).firstNotNullOfOrNull { summary ->
    val fullActivity = activityRepository.getActivity(summary.id)
    if (fullActivity != null && fullActivity.calories > 0 && fullActivity.totalIntensitySeconds > 0) {
        fullActivity
    } else null
}
```

## Considerations
- **Performance**: We limit the search to 10 activities to avoid long wait times if the user has many empty activities.
- **Data Freshness**: If no valid activity is found within the last 10, we fallback to a default "empty" signature.
- **Consistency**: Both the Metabolic Signature and the Hydration advice will use the same `validActivity` source to ensure data coherence.

## UI Changes
- No significant UI changes needed, but the labels in `MetabolicSignatureCard` will now correctly reflect data from the last *analyzable* session.
