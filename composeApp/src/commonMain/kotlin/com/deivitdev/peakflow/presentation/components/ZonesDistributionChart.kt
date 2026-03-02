package com.deivitdev.peakflow.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deivitdev.peakflow.domain.model.HeartRateZone
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*

@Composable
fun ZonesDistributionChart(
    zones: List<HeartRateZone>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        zones.forEach { zone ->
            ZoneBarItem(zone)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun ZoneBarItem(zone: HeartRateZone) {
    val zoneLabelRes = when (zone.id) {
        1 -> Res.string.zone_1
        2 -> Res.string.zone_2
        3 -> Res.string.zone_3
        4 -> Res.string.zone_4
        else -> Res.string.zone_5
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(zoneLabelRes).uppercase(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "${formatDuration(zone.durationSeconds)} (${zone.percentage.toInt()}%)",
                style = MaterialTheme.typography.labelSmall,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(zone.percentage / 100f)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(6.dp))
                    .background(zone.color)
            )
        }
    }
}

private fun formatDuration(seconds: Int): String {
    val h = seconds / 3600
    val m = (seconds % 3600) / 60
    val s = seconds % 60
    return if (h > 0) {
        "${h}:${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}"
    } else {
        "${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}"
    }
}
