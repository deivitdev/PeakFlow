## Why

Users need a centralized place to manage their personal information (name, weight, height) and application settings (Strava connection). This data is essential for accurate performance metrics and a personalized experience.

## What Changes

- Add a new "Profile" tab to the application's bottom navigation bar.
- Create a Profile screen that displays and allows editing of user data: Name, Weight, and Height.
- Move the Strava connection management (Connect/Disconnect buttons and status) from the initial "Connect" screen to this new Profile tab.
- Implement a local storage mechanism to persist these user profile details.

## Capabilities

### New Capabilities
- `user-profile`: Requirements for managing athlete personal data (Name, Weight, Height).

### Modified Capabilities
- `strava-connection-ui`: The requirement for where the connection UI is located is changing from a standalone screen to being part of the Profile tab.

## Impact

- **UI Layer**: Update `App.kt` navigation and create `ProfileScreen`.
- **Data Layer**: Extend the database schema or use a new preference store for user profile data.
- **Navigation**: Remove the "Connect" tab/screen and replace it with "Profile".
