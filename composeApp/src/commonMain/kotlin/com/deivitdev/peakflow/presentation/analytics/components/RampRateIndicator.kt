package com.deivitdev.peakflow.presentation.analytics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deivitdev.peakflow.domain.model.RampRateData
import com.deivitdev.peakflow.domain.model.RampRateStatus
import com.deivitdev.peakflow.presentation.utils.formatDecimal
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*

@Composable
fun RampRateIndicator(
    data: RampRateData,
    modifier: Modifier = Modifier
) {
    val (statusLabel, color) = when (data.status) {
        RampRateStatus.MAINTENANCE -> stringResource(Res.string.ramp_status_maintenance) to Color(0xFF9E9E9E)
        RampRateStatus.PRODUCTIVE -> stringResource(Res.string.ramp_status_productive) to Color(0xFF4CAF50)
        RampRateStatus.INTENSE -> stringResource(Res.string.ramp_status_intense) to Color(0xFFFFA000)
        RampRateStatus.RISKY -> stringResource(Res.string.ramp_status_risky) to Color(0xFFF44336)
    }

    Column(modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val formattedValue = formatDecimal(data.weeklyIncrease)
            val displayText = if (data.weeklyIncrease > 0) "+$formattedValue" else formattedValue
            Text(
                text = stringResource(Res.string.ramp_pts_per_week, displayText),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Surface(
            color = color.copy(alpha = 0.1f),
            shape = RoundedCornerShape(4.dp)
        ) {
            Text(
                text = statusLabel,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = color,
                fontSize = 10.sp,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}
