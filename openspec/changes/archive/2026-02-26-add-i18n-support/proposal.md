## Why

As the application expands to a global audience, providing support for multiple languages is crucial for user accessibility and satisfaction. Allowing users to switch between English and Spanish will make the app more inclusive and professional.

## What Changes

- Implement a localization framework using Compose Multiplatform resources.
- Add a language selection option in the User Profile section.
- Migrate all hardcoded strings to localized resource files (`strings.xml`).
- Persist the user's language preference in the local database.

## Capabilities

### New Capabilities
- `i18n-support`: Framework and infrastructure for multi-language support.

### Modified Capabilities
- `user-profile`: Adding language preference to the user's personal data.

## Impact

- **UI Layer**: Update `ProfileScreen` to include a language picker.
- **Resources**: Create `values/strings.xml` and `values-es/strings.xml` for all app strings.
- **Data Layer**: Update `UserProfile` table to include a `language` column.
- **Domain Layer**: Create use cases for retrieving and saving language preferences.
