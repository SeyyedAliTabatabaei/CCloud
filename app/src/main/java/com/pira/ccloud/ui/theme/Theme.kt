package com.pira.ccloud.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.pira.ccloud.data.model.FontSettings
import com.pira.ccloud.utils.StorageUtils

private val DarkColors = darkColorScheme(
    primary = Primary,
    secondary = Accent,
    background = Background,
    surface = Surface,
    surfaceVariant = SurfaceVariant,
    onPrimary = TextPrimary,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    error = Error
)

private val LightColors = lightColorScheme(
    primary = Primary,
    secondary = Accent,
    background = Color(0xFFF6F5FB),
    surface = Color.White,
    surfaceVariant = Color(0xFFEDEBFA),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF1A1A1A),
    onSurface = Color(0xFF1A1A1A),
    onSurfaceVariant = Color(0xFF4A4A4A),
    error = Error
)


@Composable
fun CCloudTheme(
    themeSettings: ThemeSettings = ThemeSettings(),
    fontSettings: FontSettings = FontSettings(), // Add font settings parameter
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val darkTheme = when (themeSettings.themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = when {
//        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
        darkTheme -> DarkColors
        else -> LightColors
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = dynamicTypography(1f),
        content = content
    )
}