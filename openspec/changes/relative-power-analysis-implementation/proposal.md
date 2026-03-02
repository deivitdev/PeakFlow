## Why

User weight is currently a static field in the profile with no impact on performance analysis. To provide professional-grade insights, especially for cycling, we need to normalize power data by body weight (W/kg). This allows users to understand their true athletic potential, compare efforts across different terrains, and track improvements in body composition relative to strength.

## What Changes

- **Core Calculation Logic**: Implement W/kg calculations in `CalculatePerformanceMetricsUseCase` using the user's weight from their profile.
- **Profile Summary Enhancement**: Add a "Performance Level" section to the Profile screen that displays the Relative FTP and classifies the user based on the Coggan Scale.
- **"Next Level" Analysis**: Implement logic to calculate and display the weight/power targets needed to reach the next athletic category.
- **Activity Detail Updates**: Update the Cycling detail view to show `Average W/kg` and `Max W/kg`.
- **Domain Mapping**: Ensure `PerformanceMetrics` includes relative power fields.

## Capabilities

### New Capabilities
- `relative-power-analysis`: Core logic for W/kg and category classification.

### Modified Capabilities
- `user-profile`: Update to include performance category and relative FTP in the profile overview.
- `specialized-activity-ui`: Integrate relative power metrics into cycling layouts.

## Impact

- **Domain Layer**: New use case logic for power normalization.
- **Presentation Layer**: New UI components for the Profile and Activity Detail screens.
- **User Experience**: Adds a competitive and scientific dimension to training data.
