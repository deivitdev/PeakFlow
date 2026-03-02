## 1. Domain & Data Updates

- [x] 1.1 Add `getActivity(id: String)` method to `ActivityRepository` and implementation
- [x] 1.2 Implement `GetActivityDetailUseCase` to fetch a single activity

## 2. Presentation Layer

- [x] 2.1 Create `ActivityDetailViewModel` to manage the state of a single activity
- [x] 2.2 Implement `ActivityDetailScreen` using the "Electric Pro" theme
- [x] 2.3 Create a custom `TelemetryChart` component using `Canvas` for metric visualization
- [x] 2.4 Add localized strings for the detail view (labels, units)

## 3. Navigation & Integration

- [x] 3.1 Update navigation logic in `App.kt` to support switching to the detail view
- [x] 3.2 Update `WorkoutListScreen` to trigger navigation when a card is clicked
- [x] 3.3 Implement the \"Back\" button functionality to return to the list
- [x] 3.4 Verify the detail view correctly renders data for different activity types
