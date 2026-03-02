package com.deivitdev.peakflow.presentation.analytics

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deivitdev.peakflow.domain.model.InsightType
import com.deivitdev.peakflow.domain.model.BalanceAlert
import com.deivitdev.peakflow.domain.usecase.PerformanceCategory
import com.deivitdev.peakflow.domain.usecase.RelativePowerResult
import com.deivitdev.peakflow.domain.usecase.TrendGranularity
import com.deivitdev.peakflow.domain.usecase.TrendMetric
import com.deivitdev.peakflow.presentation.components.HudTopAppBar
import com.deivitdev.peakflow.presentation.components.MetricInfoTooltip
import com.deivitdev.peakflow.presentation.components.MetricProCard
import com.deivitdev.peakflow.presentation.components.SegmentedButton
import com.deivitdev.peakflow.presentation.components.SectionTitle
import com.deivitdev.peakflow.presentation.utils.formatDecimal
import com.deivitdev.peakflow.presentation.analytics.components.*
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*

enum class AnalyticsMode {
    PERFORMANCE, INSIGHTS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(viewModel: AnalyticsViewModel, mode: AnalyticsMode = AnalyticsMode.PERFORMANCE) {
    val uiState by viewModel.uiState.collectAsState()
    val accentColor = MaterialTheme.colorScheme.primary
    val isDark = isSystemInDarkTheme()
    val scrollState = rememberScrollState()
    
    var selectedInsight by remember { mutableStateOf<CarouselInsight?>(null) }
    var showTsbGuide by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val title = when (mode) {
        AnalyticsMode.PERFORMANCE -> stringResource(Res.string.performance_tab)
        AnalyticsMode.INSIGHTS -> stringResource(Res.string.insights_tab)
    }

    Scaffold(
        topBar = {
            HudTopAppBar(
                title = title,
                actions = {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!uiState.userPhotoUrl.isNullOrBlank()) {
                            AsyncImage(
                                model = uiState.userPhotoUrl,
                                contentDescription = "Profile Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                        } else {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            androidx.compose.material3.pulltorefresh.PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = { viewModel.refresh() },
                modifier = Modifier.fillMaxSize()
            ) {
                if (uiState.isLoading && uiState.metrics == null) {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(strokeCap = StrokeCap.Round, color = MaterialTheme.colorScheme.primary)
                    }
                } else {
                    uiState.metrics?.let { metrics ->
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 20.dp)
                                .verticalScroll(scrollState)
                        ) {
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            if (mode == AnalyticsMode.PERFORMANCE) {
                                PerformanceContent(viewModel, uiState, metrics, accentColor)
                            } else {
                                InsightsContent(
                                    viewModel = viewModel, 
                                    uiState = uiState, 
                                    isDark = isDark, 
                                    onInsightClick = { selectedInsight = it },
                                    onShowTsbGuide = { showTsbGuide = true }
                                )
                            }
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }

        if (selectedInsight != null || showTsbGuide) {
            ModalBottomSheet(
                onDismissRequest = { 
                    selectedInsight = null 
                    showTsbGuide = false
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                if (showTsbGuide) {
                    TsbGuideContent(onDismiss = { showTsbGuide = false })
                } else {
                    InsightDetailContent(
                        insight = selectedInsight!!,
                        onDismiss = { selectedInsight = null }
                    )
                }
            }
        }
    }
}

@Composable
private fun TsbGuideContent(onDismiss: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.tsb_guide_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.primary,
            letterSpacing = 1.sp
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        TsbRangeItem(
            title = stringResource(Res.string.tsb_range_fresh_title),
            description = stringResource(Res.string.tsb_range_fresh_desc),
            color = Color(0xFF4CAF50)
        )
        TsbRangeItem(
            title = stringResource(Res.string.tsb_range_recovery_title),
            description = stringResource(Res.string.tsb_range_recovery_desc),
            color = Color(0xFFFFA000)
        )
        TsbRangeItem(
            title = stringResource(Res.string.tsb_range_neutral_title),
            description = stringResource(Res.string.tsb_range_neutral_desc),
            color = Color(0xFF9E9E9E)
        )
        TsbRangeItem(
            title = stringResource(Res.string.tsb_range_optimal_title),
            description = stringResource(Res.string.tsb_range_optimal_desc),
            color = Color(0xFF2196F3)
        )
        TsbRangeItem(
            title = stringResource(Res.string.tsb_range_risk_title),
            description = stringResource(Res.string.tsb_range_risk_desc),
            color = Color(0xFFF44336)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(stringResource(Res.string.back_action).uppercase(), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TsbRangeItem(title: String, description: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun InsightDetailContent(insight: CarouselInsight, onDismiss: () -> Unit) {
    val icon = when (insight.type) {
        InsightType.HYDRATION -> Icons.Default.WaterDrop
        InsightType.FUELING -> Icons.Default.LocalFireDepartment
        InsightType.GREY_ZONE -> Icons.Default.Info
        InsightType.BALANCE_ALERT -> Icons.Default.Lightbulb
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(insight.accentColor.copy(alpha = 0.1f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = insight.accentColor,
                modifier = Modifier.size(32.dp)
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = insight.title.uppercase(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Black,
            color = insight.accentColor,
            letterSpacing = 1.sp
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = insight.description,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 24.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onDismiss,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = insight.accentColor)
        ) {
            Text(
                text = stringResource(Res.string.back_action).uppercase(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PerformanceContent(
    viewModel: AnalyticsViewModel,
    uiState: AnalyticsUiState,
    metrics: com.deivitdev.peakflow.domain.model.PerformanceMetrics,
    accentColor: Color
) {
    // 1. Global Granularity Filter at the top
    SegmentedButton(
        options = listOf(TrendGranularity.WEEKLY, TrendGranularity.MONTHLY),
        selectedOption = uiState.selectedGranularity,
        onOptionSelected = { viewModel.onGranularityChange(it) },
        labelExtractor = { granularity ->
            when (granularity) {
                TrendGranularity.WEEKLY -> stringResource(Res.string.weekly)
                TrendGranularity.MONTHLY -> stringResource(Res.string.monthly)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        accentColor = accentColor
    )

    Spacer(modifier = Modifier.height(24.dp))

    // 2. Progress Overview Section (Dynamic based on granularity)
    GoalStatusSection(metrics, uiState.selectedGranularity)

    Spacer(modifier = Modifier.height(32.dp))

    val granularityLabel = when (uiState.selectedGranularity) {
        TrendGranularity.WEEKLY -> stringResource(Res.string.weekly)
        TrendGranularity.MONTHLY -> stringResource(Res.string.monthly)
    }

    SectionTitle("${stringResource(Res.string.total_performance)} ($granularityLabel)")

    // 3. Metrics Grid
    Row(Modifier.fillMaxWidth()) {
        MetricProCard(
            label = stringResource(Res.string.total_distance),
            value = formatDecimal(metrics.totalDistanceKm),
            unit = stringResource(Res.string.km),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        MetricProCard(
            label = stringResource(Res.string.total_time),
            value = "${metrics.totalDurationSeconds / 3600}",
            unit = stringResource(Res.string.hrs),
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    Row(Modifier.fillMaxWidth()) {
        MetricProCard(
            label = stringResource(Res.string.elevation_gain),
            value = "${metrics.totalElevationGainMeters.toInt()}",
            unit = stringResource(Res.string.meters),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        MetricProCard(
            label = stringResource(Res.string.activities_label),
            value = "${metrics.activityCount}",
            unit = stringResource(Res.string.num),
            modifier = Modifier.weight(1f)
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    SectionTitle(stringResource(Res.string.activity_breakdown))

    ActivityTypeBreakdownCard(
        breakdown = metrics.breakdown,
        totalDistance = metrics.totalDistanceKm,
        totalDuration = metrics.totalDurationSeconds
    )

    // 4. Trends Section
    Spacer(modifier = Modifier.height(32.dp))
    SectionTitle(stringResource(Res.string.trends_title))

    // Local Metric Selector
    SegmentedButton(
        options = listOf(TrendMetric.DISTANCE, TrendMetric.DURATION, TrendMetric.ELEVATION),
        selectedOption = uiState.selectedMetric,
        onOptionSelected = { viewModel.onMetricChange(it) },
        labelExtractor = { metric ->
            when (metric) {
                TrendMetric.DISTANCE -> stringResource(Res.string.distance)
                TrendMetric.DURATION -> stringResource(Res.string.duration)
                TrendMetric.ELEVATION -> stringResource(Res.string.elevation)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        accentColor = accentColor
    )

    Spacer(modifier = Modifier.height(16.dp))

    // Trends Chart Card with loading overlay
    Box {
        TrendsChartCard(
            dataPoints = uiState.trends,
            metric = uiState.selectedMetric,
            granularity = uiState.selectedGranularity,
            chartColor = accentColor
        )

        if (uiState.isRefreshingTrends) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        MaterialTheme.colorScheme.surface.copy(alpha = 0.4f),
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    strokeWidth = 3.dp,
                    color = accentColor
                )
            }
        }
    }
}

@Composable
private fun InsightsContent(
    viewModel: AnalyticsViewModel, 
    uiState: AnalyticsUiState, 
    isDark: Boolean,
    onInsightClick: (CarouselInsight) -> Unit,
    onShowTsbGuide: () -> Unit
) {
    // 1. Gather all dynamic insights for the carousel
    val insights = mutableListOf<CarouselInsight>()

    uiState.advancedInsights?.let { adv ->
        // Hydration Insight
        adv.hydrationAmountLiters?.let { liters ->
            val formattedLiters = ((liters * 10).toInt() / 10f).toString()
            insights.add(
                CarouselInsight(
                    type = InsightType.HYDRATION,
                    title = stringResource(Res.string.hydration_title),
                    description = stringResource(Res.string.hydration_recommendation, formattedLiters),
                    accentColor = Color(0xFF2196F3)
                )
            )
        }

        // Grey Zone Insight
        if (adv.last28DaysPolarization.isGreyZoneAlert) {
            insights.add(
                CarouselInsight(
                    type = InsightType.GREY_ZONE,
                    title = stringResource(Res.string.grey_zone_alert),
                    description = stringResource(Res.string.grey_zone_recommendation, adv.greyZonePercentage),
                    accentColor = Color(0xFF90A4AE)
                )
            )
        }
    }

    uiState.trainingRecommendation?.let { recommendation ->
        // Fueling Strategy for the selected path
        val selectedOption = recommendation.options.getOrNull(uiState.selectedPathIndex)
        selectedOption?.let { opt ->
            val strategyAdvice = when (opt.fuelingStrategy) {
                com.deivitdev.peakflow.domain.model.FuelingStrategy.LOADING -> stringResource(Res.string.fueling_strategy_loading)
                com.deivitdev.peakflow.domain.model.FuelingStrategy.PERFORMANCE -> stringResource(Res.string.fueling_strategy_performance)
                com.deivitdev.peakflow.domain.model.FuelingStrategy.MAINTENANCE -> stringResource(Res.string.fueling_strategy_maintenance)
                com.deivitdev.peakflow.domain.model.FuelingStrategy.RECOVERY -> stringResource(Res.string.fueling_strategy_recovery)
            }
            insights.add(
                CarouselInsight(
                    type = InsightType.FUELING,
                    title = stringResource(Res.string.fueling_forecast),
                    description = "${stringResource(Res.string.fueling_desc, opt.estimatedCarbGrams.toInt())} $strategyAdvice",
                    accentColor = Color(0xFFFF9800)
                )
            )
        }

        // Balance Alerts
        recommendation.priorityAlert?.let { alert ->
            val message = when (alert) {
                BalanceAlert.STRENGTH_NEGLECT -> stringResource(Res.string.alert_strength_neglect)
                BalanceAlert.HIGH_FREQUENCY -> stringResource(Res.string.alert_high_frequency)
            }
            insights.add(
                CarouselInsight(
                    type = InsightType.BALANCE_ALERT,
                    title = stringResource(Res.string.peakflow_insight),
                    description = message,
                    accentColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }

    // 2. Render Carousel at the top
    if (insights.isNotEmpty()) {
        CoachInsightCarousel(
            insights = insights,
            onInsightClick = onInsightClick
        )
        Spacer(modifier = Modifier.height(24.dp))
    }

    // 3. Training Path Selector
    uiState.trainingRecommendation?.let { recommendation ->
        SectionTitle(stringResource(Res.string.coach_path_title))
        
        DailyPathSelector(
            recommendation = recommendation,
            selectedIndex = uiState.selectedPathIndex,
            onPathSelected = viewModel::onPathSelected,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
    }

    PerformanceLevelCard(uiState.relativePower, isDark)

    uiState.fitnessFatigue?.let { ffData ->
        Spacer(modifier = Modifier.height(32.dp))
        FitnessFatigueSection(ffData, onShowTsbGuide)
    }

    uiState.advancedInsights?.let { advInsights ->
        Spacer(modifier = Modifier.height(32.dp))
        SectionTitle(stringResource(Res.string.strategic_analysis))

        PolarizationChart(
            summary = advInsights.last28DaysPolarization,
            title = stringResource(Res.string.polarization_label, stringResource(Res.string.last_28_days)),
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        MetabolicSignatureCard(advInsights.recentMetabolicSummary)
    }
}

@Composable
private fun GoalStatusSection(
    metrics: com.deivitdev.peakflow.domain.model.PerformanceMetrics,
    granularity: TrendGranularity
) {
    val accentColor = MaterialTheme.colorScheme.primary
    val progressPercent = (metrics.goalProgress * 100).toInt()
    
    val statusString = when (metrics.goalStatus) {
        "COMPLETED" -> stringResource(Res.string.status_completed)
        "AHEAD" -> stringResource(Res.string.status_ahead)
        "ON TRACK" -> stringResource(Res.string.status_on_track)
        "BEHIND" -> stringResource(Res.string.status_behind)
        else -> stringResource(Res.string.status_no_data)
    }

    val title = when (granularity) {
        TrendGranularity.WEEKLY -> stringResource(Res.string.weekly_progress)
        TrendGranularity.MONTHLY -> stringResource(Res.string.monthly_progress)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(Modifier.padding(24.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = accentColor,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(Res.string.active_phase, "$progressPercent%"),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { metrics.goalProgress },
                modifier = Modifier.fillMaxWidth().height(8.dp),
                color = accentColor,
                trackColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                strokeCap = StrokeCap.Round
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = statusString,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
private fun PerformanceLevelCard(data: RelativePowerResult?, isDark: Boolean) {
    if (data == null || data.category == PerformanceCategory.UNKNOWN || data.wKg <= 0) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.3f))
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    text = stringResource(Res.string.ftp_not_configured),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(Res.string.configure_in_profile),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
        return
    }

    val categoryColor = getCategoryColor(data.category, isDark)
    val categoryLabel = when (data.category) {
        PerformanceCategory.RECREATIONAL -> stringResource(Res.string.cat_recreational)
        PerformanceCategory.INTERMEDIATE -> stringResource(Res.string.cat_intermediate)
        PerformanceCategory.ADVANCED -> stringResource(Res.string.cat_advanced)
        PerformanceCategory.ELITE -> stringResource(Res.string.cat_elite)
        PerformanceCategory.WORLD_CLASS -> stringResource(Res.string.cat_world_class)
        PerformanceCategory.UNKNOWN -> stringResource(Res.string.cat_unknown)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, categoryColor.copy(alpha = 0.3f))
    ) {
        Column(Modifier.padding(20.dp)) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.performance_level),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = categoryLabel,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = categoryColor,
                            letterSpacing = (-0.5).sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(Res.string.relative_ftp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            MetricInfoTooltip(acronym = "FTP")
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${formatDecimal(data.wKg)} W/kg",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        MetricInfoTooltip(acronym = "WKG")
                    }
                }

            if (data.nextCategory != null) {
                Spacer(modifier = Modifier.height(20.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(16.dp))
                
                val nextLabel = when (data.nextCategory) {
                    PerformanceCategory.INTERMEDIATE -> stringResource(Res.string.cat_intermediate)
                    PerformanceCategory.ADVANCED -> stringResource(Res.string.cat_advanced)
                    PerformanceCategory.ELITE -> stringResource(Res.string.cat_elite)
                    PerformanceCategory.WORLD_CLASS -> stringResource(Res.string.cat_world_class)
                    else -> ""
                }

                Text(
                    text = stringResource(Res.string.improvement_targets),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = stringResource(Res.string.to_next_level, nextLabel),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(), 
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ImprovementChip(
                        label = stringResource(Res.string.increase_ftp, data.wattsToNextLevel?.toInt() ?: 0),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    ImprovementChip(
                        label = stringResource(Res.string.reduce_weight, formatDecimal(data.weightToNextLevel ?: 0f)),
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ImprovementChip(label: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(0.5.dp, color.copy(alpha = 0.2f)),
        modifier = modifier
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = color,
                fontSize = 10.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

private fun getCategoryColor(category: PerformanceCategory, isDark: Boolean): Color {
    return when (category) {
        PerformanceCategory.RECREATIONAL -> Color(0xFF9E9E9E)
        PerformanceCategory.INTERMEDIATE -> if (isDark) Color(0xFF2196F3) else Color(0xFF1976D2)
        PerformanceCategory.ADVANCED -> if (isDark) Color(0xFF4CAF50) else Color(0xFF388E3C)
        PerformanceCategory.ELITE -> if (isDark) Color(0xFFFF9800) else Color(0xFFF57C00)
        PerformanceCategory.WORLD_CLASS -> if (isDark) Color(0xFFF44336) else Color(0xFFD32F2F)
        PerformanceCategory.UNKNOWN -> Color.Gray
    }
}
