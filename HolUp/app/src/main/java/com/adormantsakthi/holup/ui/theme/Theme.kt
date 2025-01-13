package com.adormantsakthi.holup.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    background = darkBackground,
    primary = darkPrimary,
    secondary = darkSecondary,
    tertiary = darkTertiary,
    onSurface = bottomNavBar
)

private val LightColorScheme = lightColorScheme(

    background = lightBackground,
    primary = lightPrimary,
    secondary = lightSecondary,
    tertiary = lightTertiary,
    onSurface = lightNavBar,

)

@Composable
fun HolUpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}