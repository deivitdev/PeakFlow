# Strava API Rate Limits & Optimization

## Default Limits (Development Tier)
- **100 requests per 15 minutes**
- **1000 requests per day**

## Header Monitoring
Always check these headers in the response:
- `X-ReadRateLimit-Limit`: `100,1000` (15-min limit, daily limit)
- `X-ReadRateLimit-Usage`: `current_15min,current_day`

## Strategies to Avoid 429
1. **Incremental Sync**: Always use the `after` parameter in `GET /athlete/activities`. Pass the Unix timestamp of the most recent activity already in the database.
2. **Lazy Hydration**: Do not fetch full activity details (`GET /activities/{id}`) or streams in bulk. Only fetch when the user opens the detail view.
3. **Debounced Sync**: If using reactive observers (like SQLDelight flows), ensure that multiple database insertions don't trigger simultaneous API refreshes. Use `debounce(2000)` or a Mutex/Flag.
4. **Token Refresh**: Cache the refreshed token immediately. Check expiry locally before every call to avoid redundant `POST /oauth/token` calls.

## Error Handling
- **HttpStatusCode.TooManyRequests (429)**: The application must catch this and inform the user. Do not retry immediately; wait for the next 15-minute window or the next day if the daily limit is hit.
- **HttpStatusCode.Unauthorized (401)**: Trigger token refresh. If refresh fails, de-authorize the user locally.
