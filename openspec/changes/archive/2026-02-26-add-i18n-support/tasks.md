## 1. Data & Domain Layer

- [x] 1.1 Update `PeakFlowDatabase.sq` to add `language` column to `UserProfile` table
- [x] 1.2 Update `UserProfile` domain model and `UserRepository` to include language
- [x] 1.3 Create `SetLanguageUseCase` and `GetLanguageUseCase`

## 2. Infrastructure & Resources

- [x] 2.1 Organize `composeResources` to support `values-es`
- [x] 2.2 Migrate all hardcoded strings from screens to `strings.xml`
- [x] 2.3 Create `LanguageManager` to provide reactive locale updates

## 3. UI Implementation

- [x] 3.1 Update `ProfileScreen` to include a language selection dropdown or toggle
- [x] 3.2 Implement dynamic UI updates when the language is changed in `ProfileViewModel`
- [x] 3.3 Verify all screens (Workouts, Analytics, Profile) update correctly
