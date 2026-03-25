package com.zozi.kittyfacts.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = KittyOrange,
    onPrimary = KittyWhite,
    secondary = KittyOrangeLight,
    tertiary = KittyOrangeDark,
    background = KittyBrown,
    onBackground = KittyWhite,
    surface = KittyBrown,
    onSurface = KittyWhite,
)

private val LightColorScheme = lightColorScheme(
    primary = KittyOrange,
    onPrimary = KittyWhite,
    secondary = KittyOrangeLight,
    tertiary = KittyOrangeDark,
    background = WarmCream,
    onBackground = KittyBrown,
    surface = KittyWhite,
    onSurface = KittyBrown,
    surfaceVariant = WarmCream,
    onSurfaceVariant = KittyBrown,
)

@Composable
fun KittyFactsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+.
    // Default is false so the app keeps its brand palette.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}