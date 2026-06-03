package com.mromichov.randomcoffeeswagger

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mromichov.randomcoffeeswagger.presentation.AppNavigation
import com.mromichov.randomcoffeeswagger.presentation.theme.AppTheme
import com.mromichov.randomcoffeeswagger.presentation.theme.ThemeViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App() {
    val themeViewModel: ThemeViewModel = koinViewModel()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsStateWithLifecycle()

    AppTheme(darkTheme = isDarkTheme) {
        AppNavigation(themeViewModel::toggleTheme)
    }
}