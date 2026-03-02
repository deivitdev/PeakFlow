package com.deivitdev.peakflow.presentation.analytics.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deivitdev.peakflow.domain.model.MetabolicSummary
import com.deivitdev.peakflow.presentation.components.MetricInfoTooltip
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*

@Composable
fun MetabolicSignatureCard(
    summary: MetabolicSummary,
    modifier: Modifier = Modifier
) {
    val carbColor = Color(0xFF42A5F5) // Blue
    val fatColor = Color(0xFF66BB6A) // Green

    val totalGrams = (summary.carbGrams + summary.fatGrams).coerceAtLeast(1f)
    val carbPct = (summary.carbGrams / totalGrams) * 100f
    val fatPct = (summary.fatGrams / totalGrams) * 100f

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(Res.string.metabolic_signature),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                MetricInfoTooltip(acronym = "METABOLIC") {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Help",
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = "${summary.carbGrams.toInt()}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = "${stringResource(Res.string.carb_label)} / ",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = carbColor,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Text(
                    text = "${summary.fatGrams.toInt()}",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = stringResource(Res.string.fat_label),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = fatColor,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Box(
                    modifier = Modifier
                        .weight(carbPct.coerceAtLeast(1f))
                        .fillMaxHeight()
                        .background(carbColor)
                )
                Box(
                    modifier = Modifier
                        .weight(fatPct.coerceAtLeast(1f))
                        .fillMaxHeight()
                        .background(fatColor)
                )
            }

            if (summary.recoveryHours > 0) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(Res.string.recovery_window, summary.recoveryHours),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
