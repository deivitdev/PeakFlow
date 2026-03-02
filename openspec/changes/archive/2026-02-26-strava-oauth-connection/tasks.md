## 1. Deep Link Configuration

- [x] 1.1 Add intent-filter to `AndroidManifest.xml` for the Strava redirect scheme (`peakflow://strava-auth`)
- [x] 1.2 Implement Deep Link handling in `MainActivity.kt` to extract the `code` parameter

## 2. Core Implementation

- [x] 2.1 Update `StravaApiClient` to include the OAuth authorization URL generation
- [x] 2.2 Implement `ConnectViewModel` logic to handle the connection status and token exchange
- [x] 2.3 Create/Update the `ConnectScreen` UI with the Material 3 button and connection state

## 3. Localization & Polishing

- [x] 3.1 Add Spanish strings for "Conectar con Strava", "Conectado", and "Desconectar"
- [x] 3.2 Implement a "Desconectar" function to clear tokens from the database
- [x] 3.3 Verify the end-to-end flow from button click to activity sync
