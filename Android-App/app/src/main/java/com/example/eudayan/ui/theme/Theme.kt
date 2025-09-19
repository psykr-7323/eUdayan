package com.example.eudayan.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = WarmSalmon,
    onPrimary = AppOnPrimaryText,
    secondary = SoftCoral,
    onSecondary = AppDarkText,
    tertiary = OutlineWarmBeigeGray, // Using one of the beige/taupe for tertiary
    onTertiary = AppDarkText,
    background = BgSoftPeach,      // Your specified "Soft peach background"
    onBackground = AppDarkText,
    surface = SurfaceOffWhite,     // A very light, off-white for cards, sheets
    onSurface = AppDarkText,
    primaryContainer = SoftCoral,  // Container related to primary
    onPrimaryContainer = AppDarkText,
    secondaryContainer = BgLightCream, // A different light background for other containers
    onSecondaryContainer = AppDarkText,
    tertiaryContainer = SurfaceSubtleLightGrayF0, // A subtle gray for another level of container
    onTertiaryContainer = AppDarkText,
    error = Color(0xFFB00020),
    onError = SurfacePureWhite,       // Pure white text/icons on error color
    surfaceVariant = SurfaceLightNeutralGrayF7, // For elements needing slight distinction from surface
    onSurfaceVariant = AppDarkText,
    outline = OutlineTaupeGray      // For borders, dividers
)

@Composable
fun EudayanTheme(
    // darkTheme: Boolean = isSystemInDarkTheme(), // Parameter for dark theme if you add one
    content: @Composable () -> Unit
) {
    val colorScheme = LightColorScheme // For now, always using LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.setDecorFitsSystemWindows(window, false) // Enable edge-to-edge
            window.statusBarColor = colorScheme.primary.toArgb() // Set status bar color to new primary
            // Set status bar icons to dark as new WarmSalmon primary is light
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
