package com.pira.ccloud.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Brightness1
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FormatColorFill
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pira.ccloud.BuildConfig
import com.pira.ccloud.R
import com.pira.ccloud.data.model.SubtitleSettings
import com.pira.ccloud.data.model.VideoPlayerSettings
import com.pira.ccloud.data.model.FontSettings
import com.pira.ccloud.data.model.FontType
import com.pira.ccloud.ui.theme.ThemeMode
import com.pira.ccloud.ui.theme.ThemeSettings
import com.pira.ccloud.ui.theme.ThemeManager
import com.pira.ccloud.ui.theme.colorOptions
import com.pira.ccloud.ui.theme.defaultPrimaryColor
import com.pira.ccloud.utils.StorageUtils
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextDirection
import androidx.navigation.NavController
import com.pira.ccloud.navigation.AppScreens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.URL

@Serializable
data class GitHubRelease(
    val tag_name: String,
    val name: String,
    val html_url: String
)

@Composable
fun SettingsScreen(
    onThemeSettingsChanged: (ThemeSettings) -> Unit = {},
    onFontSettingsChanged: (FontSettings) -> Unit = {}, // Add this parameter
    navController: NavController? = null
) {
    val themeManager = ThemeManager(androidx.compose.ui.platform.LocalContext.current)
    var themeSettings by remember { mutableStateOf(themeManager.loadThemeSettings()) }
    val context = LocalContext.current
    var subtitleSettings by remember { mutableStateOf(StorageUtils.loadSubtitleSettings(context)) }
    var videoPlayerSettings by remember { mutableStateOf(StorageUtils.loadVideoPlayerSettings(context)) }
    var fontSettings by remember { mutableStateOf(StorageUtils.loadFontSettings(context)) }
    var showResetDialog by remember { mutableStateOf(false) }
    
    // Focus requesters for handling TV remote navigation
    val focusRequester = remember { FocusRequester() }
    val themeCardFocusRequester = remember { FocusRequester() }
    val videoCardFocusRequester = remember { FocusRequester() }
    val aboutCardFocusRequester = remember { FocusRequester() }
    val updateCardFocusRequester = remember { FocusRequester() }
    val resetCardFocusRequester = remember { FocusRequester() }
    
    // Configure JSON to ignore unknown keys
    val json = Json { ignoreUnknownKeys = true }
    
    // Update parent when settings change
    fun updateThemeSettings(newSettings: ThemeSettings) {
        themeSettings = newSettings
        onThemeSettingsChanged(newSettings)
        themeManager.saveThemeSettings(newSettings)
    }
    
    // Update subtitle settings
    fun updateSubtitleSettings(newSettings: SubtitleSettings) {
        subtitleSettings = newSettings
        StorageUtils.saveSubtitleSettings(context, newSettings)
    }
    
    // Update video player settings
    fun updateVideoPlayerSettings(newSettings: VideoPlayerSettings) {
        videoPlayerSettings = newSettings
        StorageUtils.saveVideoPlayerSettings(context, newSettings)
    }
    
    // Update font settings
    fun updateFontSettings(newSettings: FontSettings) {
        fontSettings = newSettings
        StorageUtils.saveFontSettings(context, newSettings)
        onFontSettingsChanged(newSettings) // Add this line to notify parent
    }
    
    // Reset all settings to defaults
    fun resetToDefaults() {
        val defaultSettings = ThemeSettings()
        updateThemeSettings(defaultSettings)
        // Reset subtitle settings to default as well
        val defaultSubtitleSettings = SubtitleSettings.getDefaultSettings(context)
        updateSubtitleSettings(defaultSubtitleSettings)
        // Reset video player settings to default as well
        val defaultVideoPlayerSettings = VideoPlayerSettings.DEFAULT
        updateVideoPlayerSettings(defaultVideoPlayerSettings)
        // Reset font settings to default as well
        val defaultFontSettings = FontSettings.DEFAULT
        updateFontSettings(defaultFontSettings)
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .focusRequester(focusRequester)
            .focusable(),
    ) {
        item {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(300)) + slideInVertically(animationSpec = tween(300)),
                exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(animationSpec = tween(300))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.settings),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    
                    // Add like icon button that navigates to favorites
                    navController?.let {
                        IconButton(
                            onClick = { navController.navigate(AppScreens.Favorites.route) },
                            modifier = Modifier
                                .size(48.dp)
                                .focusable()
                                .focusRequester(remember { FocusRequester() })
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorites",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
        
        // Theme Settings Card
        item {
            var isExpanded by remember { mutableStateOf(false) }
            val focusRequester = remember { FocusRequester() }
            
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(400)) + slideInVertically(animationSpec = tween(400, delayMillis = 100)),
                exit = fadeOut(animationSpec = tween(400)) + slideOutVertically(animationSpec = tween(400))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isExpanded = !isExpanded }
                        .focusable()
                        .focusRequester(themeCardFocusRequester)
                        .focusProperties {
                            down = videoCardFocusRequester
                        }
                        .onKeyEvent { keyEvent ->
                            when (keyEvent.key) {
                                Key.Enter, Key.Spacebar -> {
                                    isExpanded = !isExpanded
                                    true // Handled
                                }
                                else -> false // Let default handling occur
                            }
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.FormatColorFill,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = stringResource(R.string.theme_settings),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .weight(1f)
                            )
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        AnimatedVisibility(visible = isExpanded) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                
                                ThemeModeOption(
                                    mode = ThemeMode.LIGHT,
                                    label = stringResource(R.string.light),
                                    isSelected = themeSettings.themeMode == ThemeMode.LIGHT,
                                    onSelect = { mode ->
                                        val newSettings = themeSettings.copy(themeMode = mode)
                                        updateThemeSettings(newSettings)
                                    }
                                )
                                
                                ThemeModeOption(
                                    mode = ThemeMode.DARK,
                                    label = stringResource(R.string.dark),
                                    isSelected = themeSettings.themeMode == ThemeMode.DARK,
                                    onSelect = { mode ->
                                        val newSettings = themeSettings.copy(themeMode = mode)
                                        updateThemeSettings(newSettings)
                                    }
                                )
                                
                                ThemeModeOption(
                                    mode = ThemeMode.SYSTEM,
                                    label = stringResource(R.string.default_system),
                                    isSelected = themeSettings.themeMode == ThemeMode.SYSTEM,
                                    onSelect = { mode ->
                                        val newSettings = themeSettings.copy(themeMode = mode)
                                        updateThemeSettings(newSettings)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
        
        item {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(500)) + slideInVertically(animationSpec = tween(500, delayMillis = 200)),
                exit = fadeOut(animationSpec = tween(500)) + slideOutVertically(animationSpec = tween(500))
            ) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        
        // Video Player Settings Card
        item {
            var isExpanded by remember { mutableStateOf(false) }
            val focusRequester = remember { FocusRequester() }
            
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(600)) + slideInVertically(animationSpec = tween(600, delayMillis = 300)),
                exit = fadeOut(animationSpec = tween(600)) + slideOutVertically(animationSpec = tween(600))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isExpanded = !isExpanded }
                        .focusable()
                        .focusRequester(videoCardFocusRequester)
                        .focusProperties {
                            up = themeCardFocusRequester
                            down = aboutCardFocusRequester
                        }
                        .onKeyEvent { keyEvent ->
                            when (keyEvent.key) {
                                Key.Enter, Key.Spacebar -> {
                                    isExpanded = !isExpanded
                                    true // Handled
                                }
                                else -> false // Let default handling occur
                            }
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.TextFields,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = stringResource(R.string.video_player_settings),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .weight(1f)
                            )
                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                contentDescription = if (isExpanded) "Collapse" else "Expand",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        AnimatedVisibility(visible = isExpanded) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    text = stringResource(R.string.seek_time) + " : " + "${videoPlayerSettings.seekTimeSeconds}" + stringResource(R.string.seconds),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                
                                Slider(
                                    value = videoPlayerSettings.seekTimeSeconds.toFloat(),
                                    onValueChange = { seconds ->
                                        updateVideoPlayerSettings(videoPlayerSettings.copy(seekTimeSeconds = seconds.toInt()))
                                    },
                                    valueRange = 5f..30f,
                                    steps = 24, // Allow values from 5 to 30 in 1-second increments,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .focusable()
                                        .onKeyEvent { keyEvent ->
                                            when (keyEvent.key) {
                                                Key.DirectionLeft -> {
                                                    val newValue = (videoPlayerSettings.seekTimeSeconds - 1).coerceIn(5, 30).toFloat()
                                                    updateVideoPlayerSettings(videoPlayerSettings.copy(seekTimeSeconds = newValue.toInt()))
                                                    true // Handled
                                                }
                                                Key.DirectionRight -> {
                                                    val newValue = (videoPlayerSettings.seekTimeSeconds + 1).coerceIn(5, 30).toFloat()
                                                    updateVideoPlayerSettings(videoPlayerSettings.copy(seekTimeSeconds = newValue.toInt()))
                                                    true // Handled
                                                }
                                                else -> false // Let default handling occur
                                            }
                                        }
                                )
                                
                                Spacer(modifier = Modifier.height(16.dp))
                                
                                // Subtitle Settings Section
                                Text(
                                    text = stringResource(R.string.subtitle_settings),
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )
                                
                                // Text color setting
                                SubtitleColorSetting(
                                    title = stringResource(R.string.text_color),
                                    currentColor = Color(subtitleSettings.textColor),
                                    onColorSelected = { color ->
                                        updateSubtitleSettings(subtitleSettings.copy(textColor = color.toArgb()))
                                    },
                                    defaultColor = Color.Yellow
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Border color setting (Background)
                                SubtitleColorSetting(
                                    title = stringResource(R.string.background_color),
                                    currentColor = Color(subtitleSettings.borderColor),
                                    onColorSelected = { color ->
                                        updateSubtitleSettings(subtitleSettings.copy(borderColor = color.toArgb()))
                                    },
                                    noColorOption = true,
                                    glassBackgroundOption = true
                                )
                                
                                Spacer(modifier = Modifier.height(12.dp))
                                
                                // Text size setting
                                Text(
                                    text = stringResource(R.string.text_size) + " : " +  subtitleSettings.textSize.toInt().toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                
                                Slider(
                                    value = subtitleSettings.textSize,
                                    onValueChange = { size ->
                                        updateSubtitleSettings(subtitleSettings.copy(textSize = size))
                                    },
                                    valueRange = 10f..50f,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .focusable()
                                        .onKeyEvent { keyEvent ->
                                            when (keyEvent.key) {
                                                Key.DirectionLeft -> {
                                                    val newValue = (subtitleSettings.textSize - 1).coerceIn(10f, 50f)
                                                    updateSubtitleSettings(subtitleSettings.copy(textSize = newValue))
                                                    true // Handled
                                                }
                                                Key.DirectionRight -> {
                                                    val newValue = (subtitleSettings.textSize + 1).coerceIn(10f, 50f)
                                                    updateSubtitleSettings(subtitleSettings.copy(textSize = newValue))
                                                    true // Handled
                                                }
                                                else -> false // Let default handling occur
                                            }
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
        
        item {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(700)) + slideInVertically(animationSpec = tween(700, delayMillis = 400)),
                exit = fadeOut(animationSpec = tween(700)) + slideOutVertically(animationSpec = tween(700))
            ) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        
        // About Card
        item {
            val focusRequester = remember { FocusRequester() }
            
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(1000)) + slideInVertically(animationSpec = tween(1000, delayMillis = 700)),
                exit = fadeOut(animationSpec = tween(1000)) + slideOutVertically(animationSpec = tween(1000))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController?.navigate(AppScreens.About.route) }
                        .focusable()
                        .focusRequester(aboutCardFocusRequester)
                        .focusProperties {
                            up = videoCardFocusRequester
                            down = updateCardFocusRequester
                        }
                        .onKeyEvent { keyEvent ->
                            when (keyEvent.key) {
                                Key.Enter, Key.Spacebar -> {
                                    navController?.navigate(AppScreens.About.route)
                                    true // Handled
                                }
                                else -> false // Let default handling occur
                            }
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = stringResource(R.string.about),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .weight(1f)
                            )
                        }
                    }
                }
            }
        }

        item {
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(500)) + slideInVertically(animationSpec = tween(500, delayMillis = 200)),
                exit = fadeOut(animationSpec = tween(500)) + slideOutVertically(animationSpec = tween(500))
            ) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        
        // Reset to Defaults Card
        item {
            val focusRequester = remember { FocusRequester() }
            
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(animationSpec = tween(1400)) + slideInVertically(animationSpec = tween(1400, delayMillis = 1100)),
                exit = fadeOut(animationSpec = tween(1400)) + slideOutVertically(animationSpec = tween(1400))
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showResetDialog = true }
                        .focusable()
                        .focusRequester(resetCardFocusRequester)
                        .focusProperties {
                            up = updateCardFocusRequester
                        }
                        .onKeyEvent { keyEvent ->
                            when (keyEvent.key) {
                                Key.Enter, Key.Spacebar -> {
                                    showResetDialog = true
                                    true // Handled
                                }
                                else -> false // Let default handling occur
                            }
                        },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = stringResource(R.string.reset_Defaults),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .weight(1f)
                            )
                        }
                        
                        Text(
                            text = stringResource(R.string.tap_to_reset_Defaults),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
    
    // Reset confirmation dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.reset_Defaults) ,
                    style = MaterialTheme.typography.titleMedium.copy(textDirection = TextDirection.Rtl)
                )
            },
            text = {
                Text(
                    stringResource(R.string.question_reset_to_default) ,
                    style = MaterialTheme.typography.bodyMedium.copy(textDirection = TextDirection.Rtl)
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        resetToDefaults()
                        showResetDialog = false
                    }
                ) {
                    Text(stringResource(R.string.reset))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showResetDialog = false }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}

@Composable
fun ThemeModeOption(
    mode: ThemeMode,
    label: String,
    isSelected: Boolean,
    onSelect: (ThemeMode) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(mode) }
            .focusable()
            .focusRequester(focusRequester)
            .onKeyEvent { keyEvent ->
                when (keyEvent.key) {
                    Key.Enter, Key.Spacebar -> {
                        onSelect(mode)
                        true // Handled
                    }
                    else -> false // Let default handling occur
                }
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onSelect(mode) }
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
fun ColorOption(
    color: Color,
    isSelected: Boolean,
    onSelect: (Color) -> Unit,
    label: String? = null
) {
    val focusRequester = remember { FocusRequester() }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(color)
                .clickable { onSelect(color) }
                .focusable()
                .focusRequester(focusRequester)
                .onKeyEvent { keyEvent ->
                    when (keyEvent.key) {
                        Key.Enter, Key.Spacebar -> {
                            onSelect(color)
                            true // Handled
                        }
                        else -> false // Let default handling occur
                    }
                }
                .then(
                    if (isSelected) {
                        Modifier.padding(4.dp)
                    } else {
                        Modifier
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = if (color == Color.White || color == Color.Yellow) Color.Black else Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun SubtitleColorSetting(
    title: String,
    currentColor: Color,
    onColorSelected: (Color) -> Unit,
    noColorOption: Boolean = false,
    glassBackgroundOption: Boolean = false,
    defaultColor: Color? = null
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Color options
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // No color option (transparent)
                if (noColorOption) {
                    ColorOptionButton(
                        color = Color.Transparent,
                        isSelected = currentColor == Color.Transparent,
                        onClick = { onColorSelected(Color.Transparent) },
                        showBorder = true,
                        label = stringResource(R.string.empty)
                    )
                }
                
                // Glass background option (semi-transparent)
                if (glassBackgroundOption) {
                    ColorOptionButton(
                        color = Color.Black.copy(alpha = 0.5f),
                        isSelected = currentColor == Color.Black.copy(alpha = 0.5f),
                        onClick = { onColorSelected(Color.Black.copy(alpha = 0.5f)) },
                        label = stringResource(R.string.glass)
                    )
                }
                
                // Default color option
                if (defaultColor != null) {
                    ColorOptionButton(
                        color = defaultColor,
                        isSelected = currentColor == defaultColor,
                        onClick = { onColorSelected(defaultColor) }
                    )
                }
                
                // Standard color options
                listOf(Color.White, Color.Black, Color.Red, Color.Blue, Color.Green).forEach { color ->
                    ColorOptionButton(
                        color = color,
                        isSelected = currentColor == color,
                        onClick = { onColorSelected(color) }
                    )
                }
            }
            
            // Current color preview
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(currentColor)
                    .then(
                        if (currentColor == Color.Transparent) {
                            Modifier.background(Color.Gray.copy(alpha = 0.3f))
                        } else {
                            Modifier
                        }
                    )
            )
        }
    }
}

@Composable
fun ColorOptionButton(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
    showBorder: Boolean = false,
    label: String? = null
) {
    val focusRequester = remember { FocusRequester() }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    if (color == Color.Transparent && showBorder) {
                        color
                    } else {
                        color
                    }
                )
                .then(
                    if (isSelected) {
                        Modifier.background(
                            color = color,
                            shape = RoundedCornerShape(4.dp)
                        )
                    } else {
                        Modifier
                    }
                )
                .clickable(onClick = onClick)
                .focusable()
                .focusRequester(focusRequester)
                .onKeyEvent { keyEvent ->
                    when (keyEvent.key) {
                        Key.Enter, Key.Spacebar -> {
                            onClick()
                            true // Handled
                        }
                        else -> false // Let default handling occur
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selected",
                    tint = if (color == Color.White || color == Color.Yellow) Color.Black else Color.White,
                    modifier = Modifier.size(16.dp)
                )
            } else if (color == Color.Transparent && showBorder) {
                Icon(
                    imageVector = Icons.Default.Brightness1,
                    contentDescription = "No color",
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
        
        if (label != null) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun FontOption(
    fontType: FontType,
    label: String,
    isSelected: Boolean,
    onSelect: (FontType) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect(fontType) }
            .focusable()
            .focusRequester(focusRequester)
            .onKeyEvent { keyEvent ->
                when (keyEvent.key) {
                    Key.Enter, Key.Spacebar -> {
                        onSelect(fontType)
                        true // Handled
                    }
                    else -> false // Let default handling occur
                }
            }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = { onSelect(fontType) }
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}