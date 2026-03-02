package com.deivitdev.peakflow.presentation.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deivitdev.peakflow.domain.model.ActivitySplit
import com.deivitdev.peakflow.presentation.utils.formatSeconds
import com.deivitdev.peakflow.presentation.utils.formatPace
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*

@Composable
fun SplitsList(
    splits: List<ActivitySplit>,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val visibleSplits = if (isExpanded) splits else splits.take(5)
    val hasMore = splits.size > 5
    
    val fastestIndex = splits.minByOrNull { it.movingTimeSeconds }?.let { splits.indexOf(it) }
    val slowestIndex = splits.maxByOrNull { it.movingTimeSeconds }?.let { splits.indexOf(it) }

    Column(modifier = modifier.fillMaxWidth()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "KM",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.width(40.dp)
            )
            Text(
                text = "PACE",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "ELEV",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "AVG HR",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.End
            )
        }

        Column(
            modifier = Modifier.animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            visibleSplits.forEachIndexed { index, split ->
                SplitItem(
                    number = index + 1, 
                    split = split,
                    isFastest = index == fastestIndex,
                    isSlowest = index == slowestIndex
                )
            }
        }

        if (hasMore) {
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { isExpanded = !isExpanded }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val label = if (isExpanded) {
                        stringResource(Res.string.show_less)
                    } else {
                        stringResource(Res.string.show_all, splits.size)
                    }
                    Text(
                        text = label.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 1.sp
                    )
                    Spacer(Modifier.width(4.dp))
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SplitItem(
    number: Int, 
    split: ActivitySplit,
    isFastest: Boolean = false,
    isSlowest: Boolean = false
) {
    val backgroundColor = when {
        isFastest -> Color(0xFF00E676).copy(alpha = 0.15f) // Neon Green
        isSlowest -> Color(0xFFFF5252).copy(alpha = 0.15f) // Soft Red
        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
    }
    
    val numberColor = when {
        isFastest -> Color(0xFF00E676)
        isSlowest -> Color(0xFFFF5252)
        else -> MaterialTheme.colorScheme.primary
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(vertical = 8.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$number",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = numberColor,
            modifier = Modifier.width(28.dp)
        )
        
        Text(
            text = formatPace(split.averageSpeedKmh),
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Monospace,
            fontWeight = if (isFastest) FontWeight.Bold else FontWeight.Medium,
            color = if (isFastest) Color(0xFF00E676) else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "${if (split.elevationDifference > 0) "+" else ""}${split.elevationDifference.toInt()}m",
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Monospace,
            color = if (split.elevationDifference > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = "${split.averageHeartRate?.toInt() ?: "--"} bpm",
            style = MaterialTheme.typography.bodyMedium,
            fontFamily = FontFamily.Monospace,
            modifier = Modifier.weight(1f),
            textAlign = androidx.compose.ui.text.style.TextAlign.End
        )
    }
}
