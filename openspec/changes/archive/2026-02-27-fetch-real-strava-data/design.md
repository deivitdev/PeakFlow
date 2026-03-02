## Context

The current Strava integration only fetches basic activity summaries and uses random data for detail views. To provide real value, we must fetch and persist the actual time-series data (Streams) and all available summary metrics.

## Goals / Non-Goals

**Goals:**
- Extend `StravaApiClient` to support `GET /activities/{id}/streams`.
- Update `Activity` table to store `averageHeartRate`, `maxHeartRate`, and other extended metrics.
- Implement a storage mechanism for time-series data (e.g., JSON blob in the database).
- Eliminate all random data generation in the repository.

**Non-Goals:**
- Real-time fetching of streams during list scrolling (only on-demand for details).
- Multi-user data isolation (already handled by current single-user design).

## Decisions

### 1. Stream Storage: Serialized JSON Blobs
- **Rationale**: SQLDelight handles TEXT well. For time-series data that is mainly used for charting, storing the full stream as a JSON blob is simpler and faster than creating a separate relational table for thousands of points per activity.
- **Alternative**: A dedicated `StreamPoints` table. Rejected because it complicates queries and increases DB overhead for a feature that is purely visual.

### 2. API Strategy: On-demand Detail Fetching
- **Rationale**: Fetching streams for every activity in the list would hit Strava's rate limits very quickly. We will fetch summary data in bulk and detailed streams only when the user opens an activity.

### 3. Sync Depth: Increase to 100
- **Rationale**: 30 activities is too shallow for many active users. Increasing to 100 provides a better initial experience without significantly impacting sync time.

## Risks / Trade-offs

- **[Risk] Rate Limiting** → **Mitigation**: Implement a cache-first strategy. If an activity's streams are already in the DB, do not re-fetch them from the API.
- **[Risk] Database Size** → **Mitigation**: Monitor the size of the database. Since we only store streams for activities the user has viewed, the growth should be manageable.
