## Why

While the back-end logic for Strava synchronization exists, users currently lack a way to initiate the connection within the application. Providing a clear "Connect to Strava" interface in the profile section is necessary to enable users to grant permissions and start syncing their activities.

## What Changes

- Add a "Connect to Strava" button in the Profile/Connect screen.
- Implement the UI feedback for the connection state (Connected, Disconnected, Connecting).
- Integrate the OAuth2 redirect flow to trigger from the button click.
- Store the connection status and update the UI accordingly.

## Capabilities

### New Capabilities
- `strava-connection-ui`: UI requirements for the Strava connection status and interaction.

### Modified Capabilities
<!-- No requirement changes to existing specs -->

## Impact

- **UI Layer**: Update `ConnectScreen` or create a `ProfileScreen` with the connection button.
- **Navigation**: Ensure the connection flow handles external browser redirects and returns to the app.
- **State Management**: Persist and observe the connection state.
