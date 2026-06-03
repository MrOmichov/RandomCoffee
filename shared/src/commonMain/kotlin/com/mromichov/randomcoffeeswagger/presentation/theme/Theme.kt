package com.mromichov.randomcoffeeswagger.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mromichov.randomcoffeeswagger.presentation.theme.DarkColorScheme

private val LightColorScheme = lightColorScheme(
    primary = primaryBlueLight,
    background = backgroundLight,
    surface = neutral1Light,
    secondary = neutral2Light,
    tertiary = neutral3Light,
    onSurface = black
)

private val DarkColorScheme = darkColorScheme(
    primary = primaryBlueDark,
    background = backgroundDark,
    surface = neutral1Dark,
    secondary = neutral2Dark,
    tertiary = neutral3Dark,
    onSurface = Color.White
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}