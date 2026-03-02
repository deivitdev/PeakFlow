package com.deivitdev.peakflow.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SegmentedButton(
    options: List<T>,
    selectedOption: T,
    onOptionSelected: (T) -> Unit,
    labelExtractor: @Composable (T) -> String,
    modifier: Modifier = Modifier,
    accentColor: Color = MaterialTheme.colorScheme.primary
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth()
    ) {
        options.forEachIndexed { index, item ->
            val isSelected = item == selectedOption
            SegmentedButton(
                selected = isSelected,
                onClick = { onOptionSelected(item) },
                shape = when (index) {
                    0 -> RoundedCornerShape(topStartPercent = 50, bottomStartPercent = 50)
                    options.lastIndex -> RoundedCornerShape(topEndPercent = 50, bottomEndPercent = 50)
                    else -> RoundedCornerShape(0.dp)
                },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = accentColor.copy(alpha = 0.2f),
                    activeContentColor = accentColor,
                    inactiveContainerColor = MaterialTheme.colorScheme.surface,
                    inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f))
            ) {
                Text(text = labelExtractor(item))
            }
        }
    }
}
