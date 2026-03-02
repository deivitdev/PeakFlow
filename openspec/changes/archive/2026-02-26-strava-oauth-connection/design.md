## Context

The application needs to handle the Strava OAuth2 flow. This involves launching an external browser for user authorization and handling the redirect back to the app using a Deep Link.

## Goals / Non-Goals

**Goals:**
- Implement a "Connect" screen with a functional Strava button.
- Handle the OAuth2 redirect flow using Deep Links.
- Store the access/refresh tokens securely in the local database.
- Reflect the connection status in the UI.

**Non-Goals:**
- Implementing a full user profile (only the connection part for now).
- Handling token expiration/refresh (already designed in data layer, but will be integrated here).

## Decisions

### 1. OAuth Redirect: Deep Links
- **Rationale**: standard way to return to a mobile app from a browser-based authentication flow.
- **Implementation**: Define a custom scheme (e.g., `peakflow://strava-auth`) in `AndroidManifest.xml`.

### 2. Connection State: Repository-backed
- **Rationale**: The UI should observe the presence of tokens in the database to determine the connection state.
- **Alternative**: Storing state in SharedPreferences. Rejected to keep data centralized in the database.

### 3. UI: `ConnectScreen` refinement
- **Rationale**: Update the existing `ConnectScreen` placeholder to a functional Material 3 screen.

## Risks / Trade-offs

- **[Risk] Browser Redirect Failure** → **Mitigation**: Implement clear error handling if the app is not returned to correctly.
- **[Risk] Token Security** → **Mitigation**: Use SQLDelight for storage (consider encryption for production in a later change).
