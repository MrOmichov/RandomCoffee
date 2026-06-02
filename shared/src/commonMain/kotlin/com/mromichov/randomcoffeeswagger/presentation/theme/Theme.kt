package com.mromichov.randomcoffeeswagger.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = primaryBlueLight,
    background = backgroundLight,
    surface = neutral1Light,
    secondary = neutral2Light,
    tertiary = neutral3Light,
    onSurface = black
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}