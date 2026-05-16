package com.example.rahul.weathersnap.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = GreenAccent,
    secondary = GreenAccent,
    tertiary = TempYellow,
    background = DarkGray,
    surface = SurfaceGray,
    onPrimary = DarkGray,
    onSecondary = DarkGray,
    onTertiary = DarkGray,
    onBackground = WhiteText,
    onSurface = WhiteText,
    error = ErrorRed
)

@Composable
fun WeatherSnapTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
