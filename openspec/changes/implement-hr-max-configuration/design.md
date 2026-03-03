# Design: Implement HR Max Configuration

## UI Changes
- **Profile Screen**: Add an `OutlinedTextField` for "Max Heart Rate (BPM)" under the FTP field.
- **Tooltips**: Update tooltips to explain how HR Max affects zone calculations.

## Domain & Logic
- **ActivityRepositoryImpl**: The `getActivity` method will fetch the user profile first to get the correct `hrMax`.
- **ActivityDetailViewModel**: Will also pass the profile's `hrMax` to its internal zone calculation.

## Fallback Strategy
If the user hasn't set an HR Max (value is null or 0), the system will fallback to 190 BPM as a safe default.
