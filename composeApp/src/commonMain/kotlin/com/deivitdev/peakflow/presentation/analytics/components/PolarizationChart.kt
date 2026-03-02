package com.deivitdev.peakflow.presentation.analytics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deivitdev.peakflow.domain.model.PolarizationSummary
import com.deivitdev.peakflow.presentation.components.MetricInfoTooltip
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*

@Composable
fun PolarizationChart(
    summary: PolarizationSummary,
    title: String,
    modifier: Modifier = Modifier
) {
    val lowColor = Color(0xFF2196F3)   // Blue (Aerobic/Recovery)
    val midColor = Color(0xFF90A4AE)   // Gray (The literal "Grey Zone")
    val highColor = Color(0xFFEF5350)  // Red (Anaerobic/High)

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                MetricInfoTooltip(acronym = "80/20") {
                    Icon(
                        imageVector = androidx.compose.material.icons.Icons.Default.Info,
                        contentDescription = "Help",
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }
            if (summary.isGreyZoneAlert) {
                Text(
                    text = stringResource(Res.string.grey_zone_alert),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.error,
                    letterSpacing = 1.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .clip(RoundedCornerShape(6.dp))
        ) {
            if (summary.lowIntensityPercentage > 0) {
                Box(
                    modifier = Modifier
                        .weight(summary.lowIntensityPercentage.coerceAtLeast(1f))
                        .fillMaxHeight()
                        .background(lowColor)
                )
            }
            if (summary.thresholdIntensityPercentage > 0) {
                Box(
                    modifier = Modifier
                        .weight(summary.thresholdIntensityPercentage.coerceAtLeast(1f))
                        .fillMaxHeight()
                        .background(midColor)
                )
            }
            if (summary.highIntensityPercentage > 0) {
                Box(
                    modifier = Modifier
                        .weight(summary.highIntensityPercentage.coerceAtLeast(1f))
                        .fillMaxHeight()
                        .background(highColor)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LegendItem(stringResource(Res.string.low_intensity), "${summary.lowIntensityPercentage.toInt()}%", lowColor)
            LegendItem(stringResource(Res.string.threshold_intensity), "${summary.thresholdIntensityPercentage.toInt()}%", midColor)
            LegendItem(stringResource(Res.string.high_intensity), "${summary.highIntensityPercentage.toInt()}%", highColor)
        }
    }
}

@Composable
private fun LegendItem(label: String, value: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(2.dp))
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "$label: $value",
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontSize = 9.sp
        )
    }
}
