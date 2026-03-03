# Tasks: Android Health Connect Integration

## Phase 1: Infrastructure & Setup
- [x] Add `androidx.health.connect:connect-client` dependency to `libs.versions.toml` and `build.gradle.kts`.
- [x] Add required `<uses-permission>` and intent filters to `androidMain/AndroidManifest.xml`.
- [x] Define `HealthRepository` interface in `commonMain`.
- [x] Implement `AndroidHealthRepository` in `androidMain` with availability checks.

## Phase 2: Data & Persistence
- [x] Add `HealthMetric` table to `PeakFlowDatabase.sq`.
- [x] Implement `LocalHealthDataSource` to handle CRUD operations for health data.
- [x] Create `SyncHealthDataUseCase` to orchestrate fetching from Health Connect and saving to DB.
- [x] Update `UserProfile.weightKg` logic to prioritize recent Health Connect data.

## Phase 3: Domain & Logic
- [x] Implement recovery correlation algorithm (HRV trend vs. Fatigue).
- [x] Create `GetRecoveryStatusUseCase` to provide the final "Ready to Train" state.

## Phase 4: UI & UX
- [x] Design and implement `RecoveryContextCard` in `AnalyticsScreen`.
- [x] Implement `HealthInfoTooltip` component using Material 3 `RichTooltip` or a custom Dialog.
- [x] Add "Connect Health" section to `ProfileScreen` with permission request handling.
- [x] Add educational content for HRV, Sleep, and Weight metrics.

## Phase 5: Verification

- [ ] Verify permission flow on an Android 14+ device/emulator.
- [ ] Mock Health Connect data to test "Optimal", "Caution", and "Overload" recovery states.
- [ ] Ensure weight changes in Health Connect reflect in W/kg metrics.
