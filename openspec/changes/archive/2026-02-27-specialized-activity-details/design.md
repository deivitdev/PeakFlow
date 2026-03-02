## Context

The current `ActivityDetailScreen` is becoming complex as it tries to handle different types of activities (cycling, walking, strength) in a single composable. To improve maintainability and user experience, we will refactor it into a router that delegates to specialized layout components.

## Goals / Non-Goals

**Goals:**
- Decouple sport-specific UI logic from the main `ActivityDetailScreen`.
- Create 4 specialized layout composables: `CyclingDetailContent`, `WalkingDetailContent`, `StrengthDetailContent`, and `GenericDetailContent`.
- Optimize metric placement and telemetry charts for each sport.
- Reuse common components (Hero cards, HUD overlays, charts) across all layouts.

**Non-Goals:**
- Changing the underlying data fetching logic (handled by ViewModel).
- Introducing new metrics not already available in the domain model.

## Decisions

### 1. Composition Pattern: Strategy-based UI
- **Rationale**: Using a simple `when` statement on the activity type allows for clean delegation to sport-specific layouts.
- **Implementation**: `ActivityDetailScreen` will remain the entry point, but its main content will be selected dynamically.

### 2. Component Specialization
- **Cycling**: Will use a 3-column or high-density grid for Power, Cadence, and Speed. It will always show the multi-metric chart.
- **Walking**: Will maximize the Route Map area and show distance as the primary hero metric.
- **Strength**: Will omit the map entirely, using `AbstractBackdrop`. It will place the "Training Zones" chart at the center of the experience.
- **Generic**: Will use the current "Balanced" layout as a fallback.

### 3. Layout Reusability
- **Rationale**: To maintain the "Electric Pro" look, all layouts will use a shared `HudLayoutContainer` that handles the parallax effect, background map/backdrop, and glassmorphic panels.

## Risks / Trade-offs

- **[Risk] Code Duplication** → **Mitigation**: Extract shared patterns (Title + Value rows, chart sections) into small, atomic components.
- **[Risk] Navigation Complexity** → **Mitigation**: Keep navigation handled at the `App.kt` level; specialized screens only handle internal layout.
