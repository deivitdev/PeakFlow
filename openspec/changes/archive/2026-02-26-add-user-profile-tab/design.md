## Context

The application currently features a bottom navigation bar with "Workouts" and "Analytics". The "Connect" functionality was previously a standalone temporary screen. This change integrates that functionality into a permanent "Profile" tab alongside other personal user data.

## Goals / Non-Goals

**Goals:**
- Update the bottom navigation to include a "Profile" tab.
- Implement a `ProfileScreen` with input fields for Name, Weight (kg), and Height (cm).
- Relocate the Strava connection UI (status and button) from the temporary flow into the Profile screen.
- Establish a persistence layer for user profile information.

**Non-Goals:**
- Cloud synchronization of profile data (local only for now).
- Multiple user profiles.
- Profile picture upload.

## Decisions

### 1. Data Storage: New SQLDelight Table
- **Rationale**: SQLDelight is already used for activities and tokens. Adding a `UserProfile` table is consistent and allows for easy expansion (e.g., adding age or heart rate zones later).
- **Schema**:
  ```sql
  CREATE TABLE UserProfile (
      id INTEGER PRIMARY KEY CHECK (id = 0), -- Singleton table
      name TEXT NOT NULL,
      weight REAL NOT NULL,
      height REAL NOT NULL
  );
  ```

### 2. ViewModels: `ProfileViewModel`
- **Rationale**: A dedicated ViewModel will manage the state of the profile fields and the interaction with both the `ActivityRepository` (for Strava status) and a new `UserRepository` (for profile data).

### 3. UI: Bottom Navigation Update
- **Rationale**: Replacing the "Connect" logic in `App.kt` with a formal `Screen.Profile` entry.

## Risks / Trade-offs

- **[Risk] State Desync** → **Mitigation**: Use a single source of truth (the database) and expose the profile as a `Flow` to the UI.
- **[Risk] UI Clutter** → **Mitigation**: Use Material 3 sections or cards within the Profile screen to separate "Personal Data" from "Third-party Connections".
