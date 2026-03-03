package com.deivitdev.peakflow.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MetricInfoTooltip(
    acronym: String,
    modifier: Modifier = Modifier,
    content: (@Composable () -> Unit)? = null
) {
    val description = getMetricDescription(acronym)
    if (description == null) {
        content?.invoke()
        return
    }

    val tooltipState = rememberTooltipState(isPersistent = true)

    TooltipBox(
        positionProvider = TooltipDefaults.rememberTooltipPositionProvider(),
        tooltip = {
            PlainTooltip(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(12.dp)
                )
            }
        },
        state = tooltipState,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = { 
                    // Persistent tooltips on mobile show on tap
                }
            )
        ) {
            if (content != null) {
                content()
            } else {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Help",
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                )
            }
        }
    }
}

@Composable
private fun getMetricDescription(acronym: String): String? {
    val cleanAcronym = acronym.uppercase().replace(":", "").trim()
    return when {
        cleanAcronym == "FTP" -> stringResource(Res.string.ftp_desc)
        cleanAcronym.contains("STATUS") || cleanAcronym.contains("ESTADO") -> stringResource(Res.string.training_status_desc)
        cleanAcronym == "TSS" -> stringResource(Res.string.tss_desc)
        cleanAcronym == "IF" || cleanAcronym == "FI" -> stringResource(Res.string.if_desc)
        cleanAcronym == "CTL" || cleanAcronym.contains("FITNESS") -> stringResource(Res.string.ctl_desc)
        cleanAcronym == "ATL" || cleanAcronym.contains("FATIGUE") -> stringResource(Res.string.atl_desc)
        cleanAcronym == "TSB" || cleanAcronym.contains("FORM") -> stringResource(Res.string.tsb_desc)
        cleanAcronym == "W/KG" || cleanAcronym == "WKG" -> stringResource(Res.string.wkg_desc)
        cleanAcronym == "NP" || cleanAcronym.contains("NORMALIZED") -> stringResource(Res.string.np_desc)
        cleanAcronym == "HR" || cleanAcronym == "BPM" -> stringResource(Res.string.hr_desc)
        cleanAcronym == "KJ" || cleanAcronym.contains("KILOJOULES") -> stringResource(Res.string.kj_desc)
        cleanAcronym == "GAP" || cleanAcronym.contains("ADJUSTED") -> stringResource(Res.string.gap_desc)
        cleanAcronym.contains("RELATIVE EFFORT") || cleanAcronym.contains("ESFUERZO RELATIVO") -> stringResource(Res.string.tss_desc)
        cleanAcronym == "80/20" || cleanAcronym.contains("POLARIZA") -> stringResource(Res.string.polarization_desc)
        cleanAcronym == "METABOLIC" || cleanAcronym.contains("FIRMA") || cleanAcronym == "INFO" -> stringResource(Res.string.metabolic_desc)
        cleanAcronym.contains("RAMP") || cleanAcronym.contains("MEJORA") -> stringResource(Res.string.ramp_rate_desc)
        cleanAcronym == "HRV" || cleanAcronym == "VFC" -> stringResource(Res.string.hrv_desc)
        cleanAcronym == "SLEEP" || cleanAcronym == "SUEÑO" -> stringResource(Res.string.sleep_desc)
        cleanAcronym.contains("RECOVERY") || cleanAcronym.contains("RECUPERA") -> stringResource(Res.string.recovery_guide_learn_more)
        cleanAcronym == "HRMAX" || cleanAcronym.contains("PULSO") -> stringResource(Res.string.hrmax_desc)
        else -> null
    }
}
