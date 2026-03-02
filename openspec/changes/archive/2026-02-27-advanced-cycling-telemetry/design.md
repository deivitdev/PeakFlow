## Context

Cyclists rely on specific metrics like Normalized Power (NP) to understand effort intensity over variable terrain. Additionally, tracking gear and analyzing splits are standard features in high-end cycling apps.

## Goals / Non-Goals

**Goals:**
- Extend the data model to persist advanced cycling fields.
- Integrate these fields into the `CyclingDetailContent`.
- Create a reusable `SplitsList` component.

**Non-Goals:**
- Managing gear directly in PeakFlow (read-only from Strava for now).
- Historical gear analysis (kilometer accumulation per bike).

## Decisions

### 1. Data Structure for Splits: List of Objects
- **Rationale**: Splits are structured as a list of metrics (time, distance, elevation). We will store them as a JSON blob in the database, similar to how we handled streams.

### 2. UI Integration: High-Density Bento Grid
- **Rationale**: Adding 3 more power metrics plus gear requires a denser layout. We will use a smaller font size for secondary power stats (NP, Max) to keep them grouped with Average Power.

### 3. Gear Display: Sub-Header
- **Rationale**: The bike name is part of the "Identity" of the ride. It should be placed near the Location or under the Athlete's name in the header.

## Risks / Trade-offs

- **[Risk] Missing Data** → **Mitigation**: All new metrics (Power, Gear, Splits) will be optional. If Strava doesn't provide them, the corresponding UI elements will simply be omitted.
- **[Risk] Sync Time** → **Mitigation**: Fetching splits only happens during the detailed activity sync, not the bulk list sync.
