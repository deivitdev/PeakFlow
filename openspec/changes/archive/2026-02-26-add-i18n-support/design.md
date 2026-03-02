## Context

The application is built using Compose Multiplatform. Currently, it has hardcoded strings or simple resource files that don't support dynamic language switching. The data layer uses SQLDelight for local persistence.

## Goals / Non-Goals

**Goals:**
- Implement dynamic language switching without requiring an app restart.
- Store the language preference in the `UserProfile` table.
- Centralize all user-facing strings in `strings.xml` files.

**Non-Goals:**
- Supporting more than English and Spanish at this stage.
- Translating dynamic content from external APIs (e.g., activity names from Strava).

## Decisions

### 1. Localization Framework: Compose Multiplatform Resources
- **Rationale**: Since the app is built with Compose Multiplatform, using its built-in resource management is the most idiomatic and efficient approach. It supports qualifiers like `-es` for Spanish.
- **Alternative**: `moko-resources`. Rejected because the built-in Compose resources are now sufficiently mature and simpler to integrate.

### 2. State Management: `MutableStateFlow` in a Global CompositionLocal
- **Rationale**: A `CompositionLocal` providing the current `Locale` or a `LanguageManager` allows all composables to reactively update when the language changes.
- **Alternative**: Passing the language down as a parameter to every composable. Rejected as it is highly cumbersome.

### 3. Persistence: SQLDelight `UserProfile` table
- **Rationale**: We already have a `UserProfile` table. Adding a `language` column (TEXT) is the most straightforward way to persist this preference.

## Risks / Trade-offs

- **[Risk] Missing Translations** → **Mitigation**: Implement a fallback mechanism where English is used if a string is missing in Spanish.
- **[Risk] UI Overflow** → **Mitigation**: Ensure that UI components (especially buttons and labels) can handle strings of varying lengths, as Spanish strings are often longer than English ones.
