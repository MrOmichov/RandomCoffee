package com.mromichov.randomcoffeeswagger.presentation.theme

import androidx.lifecycle.ViewModel
import com.russhwolf.settings.Settings
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ThemeViewModel : ViewModel() {
    private val settings = Settings()

    private val _isDarkTheme = MutableStateFlow(
        settings.getBoolean("is_dark_theme", false)
    )
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleTheme() {
        val newValue = !_isDarkTheme.value
        _isDarkTheme.update { newValue }
        settings.putBoolean("is_dark_theme", newValue)
    }
}