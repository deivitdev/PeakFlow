package com.deivitdev.peakflow.presentation.analytics.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deivitdev.peakflow.domain.model.PathOption
import com.deivitdev.peakflow.domain.model.TrainingPathType
import com.deivitdev.peakflow.domain.model.TrainingRecommendation
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*

@Composable
fun DailyPathSelector(
    recommendation: TrainingRecommendation,
    selectedIndex: Int,
    onPathSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            itemsIndexed(recommendation.options) { index, option ->
                PathOptionCard(
                    option = option,
                    isSelected = selectedIndex == index,
                    onClick = { onPathSelected(index) },
                    modifier = Modifier.width(160.dp)
                )
            }
        }
    }
}

@Composable
fun PathOptionCard(
    option: PathOption,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val accentColor = when (option.type) {
        TrainingPathType.BUILD -> Color(0xFFEF5350) // Red
        TrainingPathType.MAINTAIN -> Color(0xFFFFB74D) // Orange
        TrainingPathType.RECOVER -> Color(0xFF66BB6A) // Green
    }

    val typeLabel = when (option.type) {
        TrainingPathType.BUILD -> stringResource(Res.string.path_build)
        TrainingPathType.MAINTAIN -> stringResource(Res.string.path_maintain)
        TrainingPathType.RECOVER -> stringResource(Res.string.path_recover)
    }

    val containerColor by animateColorAsState(
        if (isSelected) accentColor.copy(alpha = 0.1f) 
        else MaterialTheme.colorScheme.surface
    )
    val borderColor by animateColorAsState(
        if (isSelected) accentColor 
        else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
    )

    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = typeLabel,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.sp
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "${option.tssTarget}",
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Black,
                color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = stringResource(Res.string.tss_target_label),
                style = MaterialTheme.typography.labelSmall,
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            HorizontalDivider(color = borderColor.copy(alpha = 0.2f))
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val formValue = if (option.predictedTsb > 0) "+${option.predictedTsb}" else option.predictedTsb.toString()
            Text(
                text = stringResource(Res.string.form_forecast, formValue),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Black,
                color = if (option.predictedTsb < -30) Color(0xFFEF5350) else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
