## 1. Domain Data Enrichment

- [x] 1.1 Mock or extend Activity model to include heart rate and elevation time-series data
- [x] 1.2 Update `GetActivityDetailUseCase` to return enriched performance data

## 2. Interactive Chart Component

- [x] 2.1 Create `InteractiveTelemetryChart` component using `Canvas` and `Modifier.pointerInput`
- [x] 2.2 Implement vertical cursor drawing logic based on `hoveredIndex`
- [x] 2.3 Add touch detection logic to calculate the nearest data index from X-coordinate

## 3. Detail Screen Integration

- [x] 3.1 Update `ActivityDetailScreen` to manage a shared `selectedDataIndex` state
- [x] 3.2 Add \"Heart Rate\" and \"Elevation\" interactive charts to the detail layout
- [x] 3.3 Create a floating HUD component to display the value of the hovered point
- [x] 3.4 Ensure the HUD and chart labels are localized (English/Spanish)

## 4. Polishing & Testing

- [x] 4.1 Apply neón glow effects to the cursor and selected data point
- [x] 4.2 Verify synchronization between the two charts during a drag gesture
- [x] 4.3 Test the responsiveness of the touch interaction on different screen sizes
