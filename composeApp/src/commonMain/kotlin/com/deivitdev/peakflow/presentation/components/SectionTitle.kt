package com.deivitdev.peakflow.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SectionTitle(title: String, modifier: Modifier = Modifier) {
    Text(
        text = "// ${title.uppercase()}",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.primary,
        letterSpacing = 2.sp,
        modifier = modifier.padding(bottom = 12.dp)
    )
}
