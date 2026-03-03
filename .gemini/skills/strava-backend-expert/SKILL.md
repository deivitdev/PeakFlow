---
name: strava-backend-expert
description: Expert guidance for auditing, optimizing, and debugging Strava API integrations. Use when checking OAuth2 flows, rate limit management (429 errors), activity synchronization (incremental sync), and data mapping from Strava's snake_case JSON to local schemas.
---

# Strava Backend Expert

## Overview
This skill provides a specialized framework for ensuring a robust, efficient, and secure connection with the Strava API. It focuses on overcoming common pitfalls like rate limiting, OAuth inconsistencies, and data mapping errors.

## Audit Checklist

### 1. OAuth2 Flow Integrity
- **Scope**: Ensure the `scope` parameter in the authorization URL includes `read,activity:read_all` for full activity access.
- **Redirect URI**: The `redirect_uri` must be identical in both the authorization request and the token exchange (`POST /oauth/token`).
- **Token Storage**: Verify that `access_token`, `refresh_token`, and `expires_at` (Unix timestamp) are persisted.
- **Refresh Logic**: Check `expires_at` locally before every call. Refresh only if `current_time + buffer (e.g., 5 mins) > expires_at`.

### 2. Rate Limit Optimization (The 429 Prevention)
- **Incremental Sync**: When fetching activities, use the `after` parameter.
    ```kotlin
    val lastActivityTimestamp = db.getLatestActivityDate().toEpochSeconds()
    api.getActivities(after = lastActivityTimestamp)
    ```
- **Avoid Bulk Hydration**: Do not fetch details for all activities at once. Fetch detail/streams only on-demand (e.g., when entering ActivityDetailScreen).
- **Network Logging**: Use `LogLevel.INFO` or higher during development to monitor `X-ReadRateLimit-Usage` headers.
- See [rate_limits.md](references/rate_limits.md) for detailed strategies.

### 3. Data Mapping & Robustness
- **Snake Case**: Strava uses `snake_case`. Ensure `@SerialName` is used for every field in DTOs.
- **Nullability**: Almost every field except `id` can be null or missing depending on activity privacy settings or device type. Use optional types.
- **Type Safety**: Use `Long` for activity IDs. Use `Float` for distances and speeds.

## Specialized Workflows

### Debugging 429 Errors
1. Check response headers for `X-ReadRateLimit-Usage`.
2. Identify if it's a 15-minute spike or a daily limit exhaustion.
3. If spike: Implement a Mutex or Debounce in the ViewModel/Repository.
4. If daily: Switch to a different `Client ID` for development or wait for 00:00 UTC.

### Testing Synchronization
1. Clear the local `Activity` table.
2. Trigger sync and verify number of requests in logs.
3. Verify that a second sync immediately after results in **zero** new activities fetched (or only 1 request with 0 results).

## Resources
- [Rate Limits & Optimization Guide](references/rate_limits.md)
- Official Docs: [Strava API Documentation](https://developers.strava.com/docs/reference/)
