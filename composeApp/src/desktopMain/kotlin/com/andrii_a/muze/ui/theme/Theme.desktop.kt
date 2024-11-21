package com.andrii_a.muze.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun colorSchemeConfig(
    darkTheme: Boolean,
    dynamicColor: Boolean
): ColorScheme {
    return when {
        darkTheme -> darkScheme
        else -> lightScheme
    }
}

@Composable
actual fun SetupStatusBarSideEffect(isSystemInDarkMode: Boolean) {
}