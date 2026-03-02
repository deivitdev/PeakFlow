## 1. Refactoring & Base Components

- [x] 1.1 Extract common HUD layout logic into a reusable `HudLayoutContainer`
- [x] 1.2 Create `CyclingDetailContent` with focus on Power, Cadence, and Multi-metric charts
- [x] 1.3 Create `WalkingDetailContent` with focus on Map and Distance
- [x] 1.4 Create `StrengthDetailContent` using `AbstractBackdrop` and focusing on HR Zones
- [x] 1.5 Create `GenericDetailContent` as a balanced fallback layout

## 2. Dynamic Layout Integration

- [x] 2.1 Update `ActivityDetailScreen` to use a `when` statement for layout selection
- [x] 2.2 Ensure all specialized layouts correctly handle the shared `hoveredIndex` state
- [x] 2.3 Verify that the floating back button and FAB map toggle work across all layouts

## 3. Localization & Polishing

- [x] 3.1 Update `strings.xml` with any new labels required for specialized views
- [x] 3.2 Ensure \"Electric Pro\" theme consistency (neon accents, glass effects) in all 4 views
- [x] 3.3 Test each layout with real data from different Strava activity types
