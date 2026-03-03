package com.deivitdev.peakflow.presentation.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import com.deivitdev.peakflow.presentation.components.HudTopAppBar
import org.jetbrains.compose.resources.stringResource
import peakflow.composeapp.generated.resources.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import coil3.compose.AsyncImage

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    stravaAuthUrl: String,
    onDisconnectStrava: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val uriHandler = LocalUriHandler.current
    val accentColor = MaterialTheme.colorScheme.primary
    val isDark = isSystemInDarkTheme()
    val labelAlpha = if (isDark) 0.8f else 0.6f

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Profile Photo Header
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            if (!uiState.profilePhotoUrl.isNullOrBlank()) {
                AsyncImage(
                    model = uiState.profilePhotoUrl,
                    contentDescription = "Profile Photo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = androidx.compose.ui.layout.ContentScale.Crop
                )
            } else {
                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // User Data Section
        ProfileSectionTitle(stringResource(Res.string.athlete_data), isDark)
        
        OutlinedTextField(
            value = uiState.name,
            onValueChange = viewModel::onNameChange,
            label = { Text(stringResource(Res.string.name_label).uppercase(), letterSpacing = 1.sp) },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = accentColor,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = labelAlpha)
            )
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        Row(Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(Res.string.weight_label).uppercase(), 
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = labelAlpha),
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    com.deivitdev.peakflow.presentation.components.MetricInfoTooltip(acronym = "WKG")
                }
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = uiState.weight,
                    onValueChange = viewModel::onWeightChange,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = labelAlpha)
                    )
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(Res.string.height_label).uppercase(), 
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = labelAlpha),
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = uiState.height,
                    onValueChange = viewModel::onHeightChange,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = accentColor,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = labelAlpha)
                    )
                )
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.ftpWatts,
            onValueChange = viewModel::onFtpWattsChange,
            label = { Text(stringResource(Res.string.ftp_watts_label).uppercase(), letterSpacing = 1.sp) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = accentColor,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = labelAlpha)
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.weeklyGoalHours,
            onValueChange = viewModel::onWeeklyGoalChange,
            label = { Text(stringResource(Res.string.weekly_goal_label).uppercase(), letterSpacing = 1.sp) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = accentColor,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = labelAlpha)
            )
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = viewModel::saveProfile,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = accentColor)
        ) {
            Text(stringResource(Res.string.sync_profile).uppercase(), fontWeight = FontWeight.Black, letterSpacing = 1.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Appearance Section
        ProfileSectionTitle(stringResource(Res.string.display_modes), isDark)
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.1f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(stringResource(Res.string.dark_theme).uppercase(), style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Switch(
                        checked = uiState.isDarkMode == true,
                        onCheckedChange = { viewModel.onThemeChange(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.outline,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            uncheckedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
                Text(
                    text = if (uiState.isDarkMode == null) stringResource(Res.string.system_default) else if (uiState.isDarkMode == true) stringResource(Res.string.enabled) else stringResource(Res.string.disabled),
                    style = MaterialTheme.typography.labelSmall,
                    fontFamily = FontFamily.Monospace,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = labelAlpha)
                )

                Spacer(modifier = Modifier.height(24.dp))
                HorizontalDivider(color = accentColor.copy(alpha = 0.1f))
                Spacer(modifier = Modifier.height(24.dp))

                Text(stringResource(Res.string.language_label).uppercase(), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = accentColor, letterSpacing = 1.sp)
                Spacer(modifier = Modifier.height(12.dp))
                
                var expanded by remember { mutableStateOf(false) }
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = when(uiState.language) {
                                "en" -> stringResource(Res.string.english)
                                "es" -> stringResource(Res.string.spanish)
                                else -> stringResource(Res.string.system_default)
                            }.uppercase(),
                            fontWeight = FontWeight.Bold
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.8f).background(MaterialTheme.colorScheme.surface)
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.system_default)) },
                            onClick = { 
                                viewModel.onLanguageChange(null)
                                expanded = false 
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.english)) },
                            onClick = { 
                                viewModel.onLanguageChange("en")
                                expanded = false 
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(Res.string.spanish)) },
                            onClick = { 
                                viewModel.onLanguageChange("es")
                                expanded = false 
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Connections Section
        ProfileSectionTitle(stringResource(Res.string.connections), isDark)
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            border = BorderStroke(1.dp, accentColor.copy(alpha = 0.1f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text(
                    text = if (uiState.isConnectedToStrava) 
                        stringResource(Res.string.status_connected) 
                    else 
                        stringResource(Res.string.status_disconnected),
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    color = if (uiState.isConnectedToStrava) accentColor else MaterialTheme.colorScheme.error
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                if (uiState.isConnectedToStrava) {
                    OutlinedButton(
                        onClick = {
                            viewModel.disconnectStrava()
                            onDisconnectStrava()
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text(stringResource(Res.string.terminate_connection).uppercase(), fontWeight = FontWeight.Bold)
                    }
                } else {
                    Button(
                        onClick = { uriHandler.openUri(stravaAuthUrl) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = accentColor)
                    ) {
                        Text(stringResource(Res.string.initialize_strava).uppercase(), fontWeight = FontWeight.Black)
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ProfileSectionTitle(title: String, isDark: Boolean) {
    Text(
        text = "// $title",
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.primary.copy(alpha = if (isDark) 0.9f else 0.7f),
        modifier = Modifier.padding(bottom = 12.dp),
        letterSpacing = 2.sp
    )
}
