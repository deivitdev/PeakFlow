## 1. Data & Domain Layer

- [x] 1.1 Update `PeakFlowDatabase.sq` with `UserProfile` table and queries
- [x] 1.2 Create `UserRepository` interface and `UserRepositoryImpl`
- [x] 1.3 Implement domain models and Use Cases for profile management
- [x] 1.4 Add `1.sqm` migration file to handle existing database updates

## 2. Presentation Layer

- [x] 2.1 Create `ProfileViewModel` integrating user data and `ActivityRepository`
- [x] 2.2 Implement `ProfileScreen` using Material 3 components
- [x] 2.3 Add Spanish localization for all new profile strings

## 3. Navigation & Refactoring

- [x] 3.1 Add `Screen.Profile` to the navigation enum in `App.kt`
- [x] 3.2 Update `Scaffold` navigation items to include the Profile tab
- [x] 3.3 Replace the "Connect Screen Placeholder" logic with the new `ProfileScreen`
- [x] 3.4 Verify the Strava OAuth redirect still works with the new tab structure
