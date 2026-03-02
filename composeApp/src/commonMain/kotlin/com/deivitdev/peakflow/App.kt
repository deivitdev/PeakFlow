package com.deivitdev.peakflow

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler
import com.deivitdev.peakflow.theme.PeakFlowTheme
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*
import com.deivitdev.peakflow.data.remote.StravaApiClient
import com.deivitdev.peakflow.presentation.workout_list.WorkoutListScreen
import com.deivitdev.peakflow.presentation.workout_list.WorkoutListViewModel
import com.deivitdev.peakflow.domain.usecase.SyncActivitiesUseCase
import com.deivitdev.peakflow.domain.repository.ActivityRepository
import com.deivitdev.peakflow.data.repository.ActivityRepositoryImpl
import com.deivitdev.peakflow.data.local.LocalDataSource
import com.deivitdev.peakflow.db.PeakFlowDatabase
import com.deivitdev.peakflow.presentation.profile.ProfileScreen
import com.deivitdev.peakflow.presentation.profile.ProfileViewModel
import com.deivitdev.peakflow.domain.usecase.GetUserProfileUseCase
import com.deivitdev.peakflow.domain.usecase.SaveUserProfileUseCase
import com.deivitdev.peakflow.data.repository.UserRepositoryImpl
import com.deivitdev.peakflow.domain.usecase.GetPerformanceMetricsUseCase
import com.deivitdev.peakflow.presentation.analytics.AnalyticsScreen
import com.deivitdev.peakflow.presentation.analytics.AnalyticsViewModel
import com.deivitdev.peakflow.presentation.navigation.Screen
import com.deivitdev.peakflow.domain.usecase.GetActivityDetailUseCase
import com.deivitdev.peakflow.domain.usecase.CalculatePerformanceMetricsUseCase
import com.deivitdev.peakflow.domain.usecase.GetTrendsUseCase
import com.deivitdev.peakflow.domain.usecase.GetFitnessFatigueUseCase
import com.deivitdev.peakflow.domain.usecase.SyncDailyLoadUseCase
import com.deivitdev.peakflow.domain.usecase.CalculateRelativePowerUseCase
import com.deivitdev.peakflow.presentation.activity_detail.ActivityDetailScreen
import com.deivitdev.peakflow.presentation.activity_detail.ActivityDetailViewModel
import com.deivitdev.peakflow.presentation.auth.ConnectStravaScreen

@Composable
fun App(
    database: PeakFlowDatabase,
    initialProfile: com.deivitdev.peakflow.db.UserProfile? = null,
    stravaAuthCode: String? = null,
    onAuthCodeHandled: () -> Unit = {},
    stravaClientId: String = "",
    stravaClientSecret: String = ""
) {
    val userRepository = remember(database) { UserRepositoryImpl(database) }
    val userProfileFlow = remember(userRepository) { userRepository.getUserProfile() }
    val userProfile by userProfileFlow.collectAsState(initial = null)

    val effectiveProfile = userProfile ?: initialProfile?.let {
        com.deivitdev.peakflow.domain.model.UserProfile(
            name = it.name,
            weightKg = it.weight.toFloat(),
            heightCm = it.height.toFloat(),
            isDarkMode = when(it.isDarkMode) {
                0L -> false
                1L -> true
                else -> null
            },
            language = it.language,
            profilePhotoUrl = it.profilePhotoUrl
        )
    }

    val isDark = when (effectiveProfile?.isDarkMode) {
        true -> true
        false -> false
        else -> isSystemInDarkTheme()
    }

    val language = effectiveProfile?.language

    key(language) {
        PeakFlowTheme(darkTheme = isDark) {
            SystemAppearance(isDark = isDark)
            var currentScreen by remember { mutableStateOf<Screen>(Screen.Performance) }
            var previousScreen by remember { mutableStateOf<Screen>(Screen.Performance) }
            var selectedActivityId by remember { mutableStateOf<String?>(null) }
            var selectedActivityType by remember { mutableStateOf<com.deivitdev.peakflow.domain.model.WorkoutType?>(null) }
            
            var isConnected by remember { mutableStateOf(false) }

            // DI Setup
            val apiClient = remember { StravaApiClient(StravaApiClient.createDefaultHttpClient()) }
            val localDataSource = remember(database) { LocalDataSource(database) }
            val repository =
                remember(apiClient, localDataSource, stravaClientId, stravaClientSecret) {
                    ActivityRepositoryImpl(
                        apiClient = apiClient,
                        localDataSource = localDataSource,
                        clientId = stravaClientId,
                        clientSecret = stravaClientSecret
                    )
                }
            
            // Check connection status reactively
            LaunchedEffect(stravaAuthCode) {
                isConnected = repository.isConnected()
            }

            val getUserProfileUseCase =
                remember(userRepository) { GetUserProfileUseCase(userRepository) }
            val saveUserProfileUseCase =
                remember(userRepository) { SaveUserProfileUseCase(userRepository) }
            val getPerformanceMetricsUseCase =
                remember(repository, userRepository) { GetPerformanceMetricsUseCase(repository, userRepository) }
            val getActivityDetailUseCase = 
                remember(repository) { GetActivityDetailUseCase(repository) }
            val calculatePerformanceMetricsUseCase =
                remember { CalculatePerformanceMetricsUseCase() }
            val calculateRelativePowerUseCase =
                remember { CalculateRelativePowerUseCase() }
            val getTrendsUseCase =
                remember(repository) { GetTrendsUseCase(repository) }
            val syncDailyLoadUseCase = 
                remember(repository, userRepository, calculatePerformanceMetricsUseCase) { 
                    SyncDailyLoadUseCase(repository, userRepository, calculatePerformanceMetricsUseCase) 
                }
            val getFitnessFatigueUseCase =
                remember(repository) { GetFitnessFatigueUseCase(repository) }
            val calculateMetabolicUseCase = remember { com.deivitdev.peakflow.domain.usecase.CalculateMetabolicOxidationUseCase() }
            val calculatePolarizationUseCase = remember { com.deivitdev.peakflow.domain.usecase.CalculatePolarizationRatioUseCase() }
            val getAdvancedPerformanceInsightsUseCase = remember(repository, userRepository, calculateMetabolicUseCase, calculatePolarizationUseCase) {
                com.deivitdev.peakflow.domain.usecase.GetAdvancedPerformanceInsightsUseCase(repository, userRepository, calculateMetabolicUseCase, calculatePolarizationUseCase)
            }
            val predictTrainingLoadUseCase = remember { com.deivitdev.peakflow.domain.usecase.PredictTrainingLoadUseCase() }
            val auditSportBalanceUseCase = remember { com.deivitdev.peakflow.domain.usecase.AuditSportBalanceUseCase() }
            val getPredictiveCoachingUseCase = remember(repository, predictTrainingLoadUseCase, auditSportBalanceUseCase) {
                com.deivitdev.peakflow.domain.usecase.GetPredictiveCoachingUseCase(repository, predictTrainingLoadUseCase, auditSportBalanceUseCase)
            }
            val syncActivitiesUseCase = remember(repository) { SyncActivitiesUseCase(repository) }
            val observeActivitiesUseCase = remember(repository) { com.deivitdev.peakflow.domain.usecase.ObserveActivitiesUseCase(repository) }
            val workoutListViewModel = remember(repository, syncActivitiesUseCase) {
                WorkoutListViewModel(repository, syncActivitiesUseCase)
            }

            val profileViewModel =
                remember(getUserProfileUseCase, saveUserProfileUseCase, repository, calculateRelativePowerUseCase) {
                    ProfileViewModel(getUserProfileUseCase, saveUserProfileUseCase, repository, calculateRelativePowerUseCase)
                }
            val analyticsViewModel = remember(getPerformanceMetricsUseCase, getTrendsUseCase, getFitnessFatigueUseCase, syncDailyLoadUseCase, getUserProfileUseCase, calculateRelativePowerUseCase, getAdvancedPerformanceInsightsUseCase, getPredictiveCoachingUseCase, observeActivitiesUseCase, syncActivitiesUseCase) {
                AnalyticsViewModel(getPerformanceMetricsUseCase, getTrendsUseCase, getFitnessFatigueUseCase, syncDailyLoadUseCase, getUserProfileUseCase, calculateRelativePowerUseCase, getAdvancedPerformanceInsightsUseCase, getPredictiveCoachingUseCase, observeActivitiesUseCase, syncActivitiesUseCase)
            }
            val activityDetailViewModel = remember(getActivityDetailUseCase, calculatePerformanceMetricsUseCase, getUserProfileUseCase) {
                ActivityDetailViewModel(getActivityDetailUseCase, calculatePerformanceMetricsUseCase, getUserProfileUseCase)
            }

            val authUrl = remember(stravaClientId) {
                apiClient.getAuthorizationUrl(
                    clientId = stravaClientId,
                    redirectUri = "peakflow://strava-auth"
                )
            }
            
            val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current

            // Automatic Sync on Launch
            LaunchedEffect(Unit) {
                isConnected = repository.isConnected()
                if (isConnected) {
                    repository.syncActivities()
                    profileViewModel.syncStravaProfile()
                    analyticsViewModel.loadData()
                    workoutListViewModel.loadActivities()
                }
            }

            BackHandler(enabled = currentScreen == Screen.ActivityDetail) {
                activityDetailViewModel.clearState()
                currentScreen = previousScreen
            }

            // Connect logic
            LaunchedEffect(stravaAuthCode) {
                if (stravaAuthCode != null) {
                    repository.connect(stravaAuthCode)
                    isConnected = repository.isConnected()
                    if (isConnected) {
                        profileViewModel.syncStravaProfile()
                        repository.syncActivities()
                        analyticsViewModel.loadData()
                        workoutListViewModel.loadActivities()
                        currentScreen = Screen.Performance
                        previousScreen = Screen.Performance
                    }
                    onAuthCodeHandled()
                }
            }

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                containerColor = MaterialTheme.colorScheme.background,
                bottomBar = {
                    if (isConnected && currentScreen != Screen.ActivityDetail) {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.background,
                            tonalElevation = 0.dp
                        ) {
                            Screen.entries.filter { it != Screen.ActivityDetail }.forEach { screen ->
                                NavigationBarItem(
                                    selected = currentScreen == screen,
                                    onClick = { 
                                        previousScreen = screen
                                        currentScreen = screen 
                                    },
                                    icon = { Icon(screen.icon, contentDescription = screen.title) },
                                    colors = NavigationBarItemDefaults.colors(
                                        selectedIconColor = MaterialTheme.colorScheme.primary,
                                        selectedTextColor = MaterialTheme.colorScheme.primary,
                                        unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.5f
                                        ),
                                        unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                                            alpha = 0.5f
                                        ),
                                        indicatorColor = Color.Transparent
                                    ),
                                    label = {
                                        Text(
                                            text = when (screen) {
                                                Screen.Workouts -> stringResource(Res.string.workouts_title)
                                                Screen.Performance -> stringResource(Res.string.performance_tab)
                                                Screen.Insights -> stringResource(Res.string.insights_tab)
                                                Screen.Profile -> stringResource(Res.string.profile_tab)
                                                else -> ""
                                            }.uppercase(),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
            ) { padding ->
                Box(Modifier
                    .fillMaxSize()
                    .then(
                        if (isConnected && currentScreen != Screen.ActivityDetail) {
                            Modifier.padding(padding)
                        } else {
                            Modifier
                        }
                    )) {
                    if (!isConnected) {
                        ConnectStravaScreen(onConnectClick = { uriHandler.openUri(authUrl) })
                    } else {
                        when (currentScreen) {
                            Screen.Workouts -> {
                                WorkoutListScreen(
                                    viewModel = workoutListViewModel,
                                    onActivityClick = { id, type ->
                                        selectedActivityId = id
                                        selectedActivityType = type
                                        previousScreen = Screen.Workouts
                                        currentScreen = Screen.ActivityDetail
                                    }
                                )
                            }

                            Screen.Performance -> {
                                AnalyticsScreen(analyticsViewModel, mode = com.deivitdev.peakflow.presentation.analytics.AnalyticsMode.PERFORMANCE)
                            }

                            Screen.Insights -> {
                                AnalyticsScreen(analyticsViewModel, mode = com.deivitdev.peakflow.presentation.analytics.AnalyticsMode.INSIGHTS)
                            }

                            Screen.Profile -> {
                                ProfileScreen(
                                    viewModel = profileViewModel, 
                                    stravaAuthUrl = authUrl,
                                    onDisconnectStrava = { isConnected = false }
                                )
                            }
                            
                            Screen.ActivityDetail -> {
                                selectedActivityId?.let { id ->
                                    ActivityDetailScreen(
                                        id = id,
                                        type = selectedActivityType,
                                        viewModel = activityDetailViewModel,
                                        onBack = { 
                                            activityDetailViewModel.clearState()
                                            currentScreen = previousScreen 
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
