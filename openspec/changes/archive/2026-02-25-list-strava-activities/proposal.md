## Why

Now that the data layer and Strava synchronization are implemented, users need a functional interface to view their synchronized activities. This screen is essential for users to verify their data and navigate to deeper analysis of their performance.

## What Changes

- Implement a new "Workouts" screen that lists activities from the local database.
- Create a visual representation for different types of activities (Cycling, Strength, etc.).
- Add "Pull-to-refresh" functionality to trigger synchronization with Strava.
- Localize the list view to Spanish as per project requirements.

## Capabilities

### New Capabilities
- `activity-list-ui`: Requirements for the activity list interface, including layout and interactions.

### Modified Capabilities
<!-- No requirement changes to existing specs -->

## Impact

- **UI Layer**: Creation of `WorkoutListScreen` and associated ViewModels.
- **Resources**: New strings and icons for the list items.
- **Navigation**: Integration of the screen into the main application navigation flow.
