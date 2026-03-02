## Why

The current "Parallax HUD" layout (map as background) and the previous "BottomSheet" attempt either make interaction difficult or feel disjointed from the activity details. Users need a way to see a compact map preview that feels integrated into the athlete's data grid but can expand smoothly into a high-fidelity interactive view without losing context.

## What Changes

- **Bento Map Thumbnail**: Replace the background map with a compact "Mini-Map" card integrated into the Activity Detail metrics grid.
- **Morphing Expansion Animation**: Implement a smooth transition where the small map thumbnail "morphs" and scales up to fill the screen when tapped.
- **Dedicated Map Interaction Mode**: In full-screen mode, the map becomes fully interactive (zoom, scroll, rotate) and features its own minimal HUD (back/close button).
- **Abstract Backdrop Fallback**: Keep the `AbstractBackdrop` for activities without a route, ensuring consistent UI spacing.

## Capabilities

### New Capabilities
- `morphing-transitions`: Requirements for smooth, context-preserving UI transitions between layout states.

### Modified Capabilities
- `activity-detail-ui`: Update the map integration strategy from background-parallax to a bento-style expanding portal.

## Impact

- **UI Layer**: Major refactoring of `ActivityDetailScreen.kt` layers.
- **Navigation**: The "Full Screen Map" will be handled as an internal state transition within the detail screen rather than a separate navigation destination (or a highly synchronized one).
- **Components**: Update `InteractiveMap` to support "preview" vs "interactive" modes effectively within the same screen lifecycle.
