## 1. Preparation & ViewModels

- [x] 1.1 Create `WorkoutListViewModel` to fetch activities from `ActivityRepository`
- [x] 1.2 Implement `WorkoutListUiState` to handle data, loading, and refresh states
- [x] 1.3 Add logic to trigger `SyncActivitiesUseCase` from the ViewModel

## 2. UI Implementation

- [x] 2.1 Implement `WorkoutListScreen` using `LazyColumn` for efficiency
- [x] 2.2 Create a localized `WorkoutItem` card to display individual activity details
- [x] 2.3 Integrate Material 3 icons for different activity types (Cycling, Strength)
- [x] 2.4 Add `PullRefresh` interaction to trigger data synchronization

## 3. Integration & Navigation

- [x] 3.1 Update `App.kt` to set `WorkoutListScreen` as the initial screen
- [x] 3.2 Ensure localized Spanish strings are used for all UI elements
- [x] 3.3 Verify synchronization state is correctly reflected in the UI (loading indicators)
