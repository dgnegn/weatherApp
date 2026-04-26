package com.aplussoft.weatherapp.core.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val WeatherLightColors = lightColorScheme(
    primary = WeatherColors.Accent,
    surfaceVariant = WeatherColors.Sunny,
    secondary = WeatherColors.Cloudy,
    background = WeatherColors.Background,
    surface = WeatherColors.Background,
    onPrimary = WeatherColors.PrimaryText,
    onSecondary = WeatherColors.PrimaryText,
    onBackground = WeatherColors.PrimaryText,
    onSurface = WeatherColors.PrimaryText
)

private val WeatherDarkColors = darkColorScheme(
    primary = WeatherColors.Accent,
    surfaceVariant = WeatherColors.Stormy,
    secondary = WeatherColors.Cloudy,
    background = Color.Black,
    surface = Color.Black,
    onPrimary = WeatherColors.PrimaryText,
    onSecondary = WeatherColors.SecondaryText,
    onBackground = WeatherColors.PrimaryText,
    onSurface = WeatherColors.PrimaryText
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val darkTheme = isSystemInDarkTheme()
    val colorScheme = if (darkTheme) WeatherDarkColors else WeatherLightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


@Composable
fun AnimatedBackground(isDay: Boolean) {
    val transition = rememberInfiniteTransition(label = "BackgroundTransition")

    // Refined color pairs for a more "premium" look
    val color1 by transition.animateColor(
        initialValue = if (isDay) Color(0xFF2193b0) else Color(0xFF0f2027),
        targetValue = if (isDay) Color(0xFF1e3c72) else Color(0xFF203A43),
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "TopColor"
    )

    val color2 by transition.animateColor(
        initialValue = if (isDay) Color(0xFF6dd5ed) else Color(0xFF2c5364),
        targetValue = if (isDay) Color(0xFF2a5298) else Color(0xFF000000),
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "BottomColor"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(color1, color2)
                )
            )
    )
}
