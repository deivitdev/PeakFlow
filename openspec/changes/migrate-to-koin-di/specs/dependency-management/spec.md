## ADDED Requirements

### Requirement: Centralized Dependency Graph
The system SHALL use Koin to manage the lifecycle and injection of all Data Sources, Repositories, Use Cases, and ViewModels.

#### Scenario: Successful Injection
- **WHEN** a Composable requests a ViewModel via `koinViewModel()`
- **THEN** Koin provides the singleton or factory instance with all its dependencies resolved

### Requirement: Multiplatform Compatibility
The dependency injection system MUST support both Android and iOS targets, handling platform-specific dependencies (like SqlDriver) through Koin modules.

#### Scenario: Platform-specific resolve
- **WHEN** the app starts on Android
- **THEN** Koin resolves the Android-specific implementation of HealthRepository
