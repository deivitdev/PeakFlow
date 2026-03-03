# Tasks: Implement HR Max Configuration

## Phase 1: UI & ViewModel
- [x] Add `hrMax` to `ProfileUiState`.
- [x] Implement `onHrMaxChange` in `ProfileViewModel`.
- [x] Add "Max Heart Rate" input field to `ProfileScreen`.
- [x] Add "HRMAX" description to `MetricInfoTooltip`.

## Phase 2: Repository & Detail
- [x] Update `ActivityRepositoryImpl.getActivity` to use user's `hrMax`.
- [x] Update `ActivityDetailViewModel.loadActivity` to use user's `hrMax`.
- [x] Verify that changing HR Max in profile immediately affects new activity details.
