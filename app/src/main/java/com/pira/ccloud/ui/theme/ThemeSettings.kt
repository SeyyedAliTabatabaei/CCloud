package com.pira.ccloud.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color

data class ThemeSettings(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val primaryColor: Color = Primary
)

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

val defaultPrimaryColor = Primary

val colorOptions = listOf(
    Primary,
    Accent,
    Color(0xFF5E35B1),
    Color(0xFF3B5BA9),
    Color(0xFF006D32)
)

@Composable
fun rememberThemeSettings(): ThemeSettings {
    return remember { ThemeSettings() }
}