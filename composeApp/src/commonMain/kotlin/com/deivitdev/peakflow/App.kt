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
import com.deivitdev.peakflow.domain.repository.ActivityRepository
import com.deivitdev.peakflow.presentation.profile.ProfileScreen
import com.deivitdev.peakflow.presentation.profile.ProfileViewModel
import com.deivitdev.peakflow.domain.usecase.*
import com.deivitdev.peakflow.domain.model.StravaConfig
import com.deivitdev.peakflow.domain.repository.UserRepository
import com.deivitdev.peakflow.presentation.analytics.AnalyticsScreen
import com.deivitdev.peakflow.presentation.analytics.AnalyticsViewModel
import com.deivitdev.peakflow.presentation.navigation.Screen
import com.deivitdev.peakflow.presentation.activity_detail.ActivityDetailScreen
import com.deivitdev.peakflow.presentation.activity_detail.ActivityDetailViewModel
import com.deivitdev.peakflow.presentation.auth.ConnectStravaScreen
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect

@Composable
fun App(
    initialProfile: com.deivitdev.peakflow.db.UserProfile? = null,
    stravaAuthCode: String? = null,
    onAuthCodeHandled: () -> Unit = {}
) {
    val userRepository: UserRepository = koinInject()
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
            var isConnecting by remember { mutableStateOf(false) }
            val snackbarHostState = remember { SnackbarHostState() }
            val coroutineScope = rememberCoroutineScope()

            // DI provided by Koin
            val apiClient: StravaApiClient = koinInject()
            val repository: ActivityRepository = koinInject()
            val stravaConfig: StravaConfig = koinInject()
            
            val workoutListViewModel: WorkoutListViewModel = koinViewModel()
            val profileViewModel: ProfileViewModel = koinViewModel()
            val analyticsViewModel: AnalyticsViewModel = koinViewModel()
            val activityDetailViewModel: ActivityDetailViewModel = koinViewModel()

            // Reactive connection status
            LaunchedEffect(repository) {
                repository.connectionStatusFlow().collect {
                    isConnected = it
                }
            }

            val authUrl = remember(stravaConfig.clientId) {
                apiClient.getAuthorizationUrl(
                    clientId = stravaConfig.clientId,
                    redirectUri = stravaConfig.redirectUri
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

            // Consolidated Connect logic
            LaunchedEffect(stravaAuthCode) {
                val code = stravaAuthCode
                if (code != null) {
                    isConnecting = true
                    println("STRAVA_AUTH: Attempting to connect with code...")
                    val connectResult = repository.connect(code)
                    
                    if (connectResult.isSuccess) {
                        println("STRAVA_AUTH: Connection successful, starting sync.")
                        currentScreen = Screen.Performance
                        previousScreen = Screen.Performance
                        
                        // Sync profile and start initial sync in background
                        coroutineScope.launch {
                            profileViewModel.syncStravaProfile()
                            val syncResult = repository.syncActivities()
                            if (syncResult.isFailure) {
                                val error = syncResult.exceptionOrNull()
                                val message = if (error is com.deivitdev.peakflow.data.remote.StravaRateLimitException) {
                                    error.message
                                } else {
                                    "Initial sync failed: ${error?.message}"
                                }
                                snackbarHostState.showSnackbar(message ?: "Unknown sync error")
                            }
                        }
                    } else {
                        val errorMsg = connectResult.exceptionOrNull()?.message ?: "Unknown error"
                        println("STRAVA_AUTH: Connection failed: $errorMsg")
                        snackbarHostState.showSnackbar("Connection failed: $errorMsg")
                    }
                    isConnecting = false
                    onAuthCodeHandled() // Move to the end to avoid cancelling this LaunchedEffect
                }
            }

            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                containerColor = MaterialTheme.colorScheme.background,
                snackbarHost = { SnackbarHost(snackbarHostState) },
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
                        Box(Modifier.fillMaxSize()) {
                            ConnectStravaScreen(onConnectClick = { uriHandler.openUri(authUrl) })
                            
                            if (isConnecting) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Black.copy(alpha = 0.5f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
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
