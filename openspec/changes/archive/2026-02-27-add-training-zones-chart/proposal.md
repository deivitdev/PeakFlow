## Why

Athletes need to visualize the intensity distribution of their workouts to ensure they are training at the correct intensities and to track their progress over time. A training zones chart provides a clear breakdown of time spent in different physiological states (e.g., Z1-Z5).

## What Changes

- Implement a new "Training Zones" distribution chart in the `ActivityDetailScreen`.
- Calculate time spent in each heart rate zone based on the activity's heart rate data.
- Integrate the chart into the "Electric Pro" design language with appropriate color coding for each zone.

## Capabilities

### New Capabilities
- `training-zones-distribution`: Requirements for visualizing time spent in heart rate zones.

### Modified Capabilities
<!-- No requirement changes to existing specs -->

## Impact

- **UI Layer**: Add the zones chart to `ActivityDetailScreen`.
- **Presentation Layer**: Update `ActivityDetailViewModel` to process heart rate data into zone buckets.
- **Components**: Create a new `ZonesBarChart` component.
