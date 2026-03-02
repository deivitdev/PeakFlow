package com.deivitdev.peakflow.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MetricItem(
    label: String,
    value: String,
    unit: String,
    modifier: Modifier = Modifier,
    hasPowerMeter: Boolean = false,
    subMetrics: List<Pair<String, String>> = emptyList()
) {
    val accentColor = MaterialTheme.colorScheme.primary
    Column(modifier) {
        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Text(
                    text = label.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(end = 4.dp)
                )
                MetricInfoTooltip(acronym = label)
            }
            if (hasPowerMeter) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(accentColor.copy(alpha = 0.1f))
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text("SENSOR", style = MaterialTheme.typography.labelSmall, color = accentColor, fontSize = 7.sp, fontWeight = FontWeight.Black)
                }
            }
        }
        Row(verticalAlignment = androidx.compose.ui.Alignment.Bottom) {
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black, fontFamily = FontFamily.Monospace)
            if (unit.isNotEmpty()) {
                Spacer(Modifier.width(4.dp))
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Text(unit, style = MaterialTheme.typography.labelSmall, color = accentColor, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 2.dp))
                    Spacer(Modifier.width(2.dp))
                    MetricInfoTooltip(
                        acronym = unit,
                        modifier = Modifier.padding(bottom = 2.dp)
                    )
                }
            }
        }
        
        if (subMetrics.isNotEmpty()) {
            Row(
                modifier = Modifier.padding(top = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                subMetrics.forEach { (subLabel, subValue) ->
                    Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                        Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                            Text(
                                text = "$subLabel:",
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 8.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Spacer(Modifier.width(2.dp))
                            MetricInfoTooltip(acronym = subLabel)
                        }
                        Spacer(Modifier.width(2.dp))
                        Text(
                            text = subValue,
                            style = MaterialTheme.typography.labelSmall,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}
