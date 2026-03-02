## Context

The current "Parallax HUD" layout uses the map as a fixed background layer. While visually interesting, it makes the map less interactive and can be confusing when activities don't have geographic data. We need a more flexible approach that treats the map as a standard "Bento" component that can be expanded on demand.

## Goals / Non-Goals

**Goals:**
- Move the map from the background to a dedicated thumbnail card within the `ActivityDetailScreen`.
- Implement a smooth state-based transition between "Thumbnail" and "Full Screen" modes.
- Ensure the full-screen mode provides standard map interactivity (zoom, rotation).
- Maintain consistent layout for indoor activities (using the abstract backdrop).

**Non-Goals:**
- Creating a separate navigation route for the map (keep it as a state within the detail screen for maximum performance/smoothness).

## Decisions

### 1. Unified Layer Strategy
- **Decision**: The `InteractiveMap` will be rendered in a top-level `Box` in the `ActivityDetailScreen`.
- **Logic**: Its size and position will be controlled by a `isMapExpanded` state variable. When `false`, its bounds match a placeholder card in the scrollable content. When `true`, it expands to `fillMaxSize()`.

### 2. Transition Implementation: `animateDpAsState` & `animateFloatAsState`
- **Decision**: Instead of complex shared element APIs (which can be tricky in KMP), we will use standard Compose animations to interpolate the padding, corner radius, and size of the map container.
- **Rationale**: Provides a "morphing" feel that is very performant and easier to debug.

### 3. Metric Layout Adjustment
- **Decision**: For cycling and walking, the map thumbnail will take a prominent spot (e.g., full width or a large square) right after the primary hero metrics.

## Risks / Trade-offs

- **[Risk] Map Flickering** → **Mitigation**: Ensure the `InteractiveMap` component is not disposed/re-created during the transition. Use a `Box` overlay that remains active.
- **[Risk] Gesture Conflicts** → **Mitigation**: Disable all map gestures when `isMapExpanded == false` to allow normal page scrolling.
