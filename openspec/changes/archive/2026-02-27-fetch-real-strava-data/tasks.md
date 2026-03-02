## 1. Data Layer Updates

- [x] 1.1 Update `PeakFlowDatabase.sq` to include extended activity fields and `streams` column
- [x] 1.2 Implement `4.sqm` migration to update the `Activity` table schema
- [x] 1.3 Add `getStreams` and `getActivityDetail` endpoints to `StravaApiClient`
- [x] 1.4 Update `StravaActivityDto` to include `average_heartrate`, `max_heartrate`, etc.

## 2. Repository & Mapper Refinement

- [x] 2.1 Update `toDb` and `toDomain` mappers to handle new extended fields
- [x] 2.2 Implement JSON serialization for heart rate and elevation streams
- [x] 2.3 Refactor `ActivityRepositoryImpl.getActivity` to fetch real data from API/DB
- [x] 2.4 Increase sync limit to 100 in `ActivityRepositoryImpl.syncActivities`

## 3. UI & Verification

- [x] 3.1 Update `ActivityDetailViewModel` to display real heart rate and elevation data
- [x] 3.2 Remove all random data generation from the domain and data layers
- [x] 3.3 Verify that charts correctly render real Strava telemetry
- [x] 3.4 Ensure the \"Aurora Bento\" theme correctly highlights real data points
