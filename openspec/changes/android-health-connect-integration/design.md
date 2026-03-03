# Design: Android Health Connect Integration

## Architecture
We will use a bridge pattern to keep the domain logic in `commonMain` while the implementation lives in `androidMain`.

- **Domain**: `HealthRepository` interface.
- **Data (Android)**: `AndroidHealthRepository` using `androidx.health.connect:connect-client`.
- **UI**: A new "Health Insights" card in `AnalyticsScreen`.

## Database Schema
A new table `HealthMetric` will be added to `PeakFlowDatabase.sq`:
```sql
CREATE TABLE HealthMetric (
    date TEXT NOT NULL PRIMARY KEY,
    weightKg REAL,
    sleepMinutes INTEGER,
    hrvMs REAL
);
```

## Synchronization Workflow
1. **App Launch**: Trigger `SyncHealthDataUseCase`.
2. **Permission Check**: If permissions are missing, show a "Connect" button in Profile.
3. **Fetch & Map**: Query Health Connect for the last 7 days of Weight, Sleep, and HRV.
4. **Update Profile**: If a newer weight is found, update `UserProfile.weightKg`.
5. **Persist Metrics**: Save daily values to `HealthMetric` table.

## Recovery Correlation Logic
The system will calculate a `RecoveryStatus` by comparing the last 7-day average of HRV against the current day's reading, weighted by the athlete's Fatigue (ATL) from the PMC model.

## UI: Educational Framework
To ensure the user understands these complex metrics:
- **Info Icons**: Every metric (HRV, Sleep, Weight) will have a `(?)` icon.
- **Educational Modals**: Tapping an icon opens a BottomSheet with:
    - Clear definition of the metric.
    - Impact on training (e.g., "High HRV = Good recovery").
    - Calculation method.
- **Status Link**: A "Learn more about recovery" link at the bottom of the card leading to a detailed internal or external guide.
