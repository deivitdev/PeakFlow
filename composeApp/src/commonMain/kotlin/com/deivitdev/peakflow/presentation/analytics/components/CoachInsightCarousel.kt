package com.deivitdev.peakflow.presentation.analytics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deivitdev.peakflow.domain.model.InsightType
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*

data class CarouselInsight(
    val type: InsightType,
    val title: String,
    val description: String,
    val accentColor: Color
)

@Composable
fun CoachInsightCarousel(
    insights: List<CarouselInsight>,
    onInsightClick: (CarouselInsight) -> Unit,
    modifier: Modifier = Modifier
) {
    if (insights.isEmpty()) return

    Column(modifier = modifier.fillMaxWidth()) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 8.dp)
        ) {
            items(insights) { insight ->
                InsightCarouselCard(insight, onClick = { onInsightClick(insight) })
            }
        }
    }
}

@Composable
private fun InsightCarouselCard(
    insight: CarouselInsight,
    onClick: () -> Unit
) {
    val icon = when (insight.type) {
        InsightType.HYDRATION -> Icons.Default.WaterDrop
        InsightType.FUELING -> Icons.Default.LocalFireDepartment
        InsightType.GREY_ZONE -> Icons.Default.Info
        InsightType.BALANCE_ALERT -> Icons.Default.Lightbulb
    }

    Card(
        onClick = onClick,
        modifier = Modifier.width(280.dp).height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = insight.accentColor.copy(alpha = 0.1f),
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(insight.accentColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = insight.accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Text(
                    text = insight.title.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = insight.accentColor,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = insight.description,
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp,
                    maxLines = 3,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }
        }
    }
}
