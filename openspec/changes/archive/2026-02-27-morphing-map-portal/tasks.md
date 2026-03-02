## 1. Refactoring Screen Layers

- [x] 1.1 Remove map from global background layer in `ActivityDetailScreen.kt`
- [x] 1.2 Update `HudLayoutContainer` to accept a map placeholder slot or handle map state internally
- [x] 1.3 Add `isMapExpanded` state to `ActivityDetailScreen`

## 2. Integrated Mini-Map Card

- [x] 2.1 Create a `MapThumbnailCard` component that wraps `InteractiveMap` in non-interactive mode
- [x] 2.2 Add the thumbnail card to `CyclingDetailContent` and `WalkingDetailContent`
- [x] 2.3 Ensure the thumbnail correctly reflects the route bounds at a fixed zoom

## 3. Morphing Animation

- [x] 3.1 Implement the expansion logic using `BoxWithConstraints` to track map geometry
- [x] 3.2 Add smooth transitions for size, position, and corner radius
- [x] 3.3 Create a \"Close Map\" button that appears only when the map is expanded

## 4. Verification & Polish

- [x] 4.1 Verify that map gestures are only enabled in expanded mode
- [x] 4.2 Ensure back navigation correctly collapses the map before leaving the screen
- [x] 4.3 Test responsiveness on different screen aspect ratios
