package com.andrii_a.muze.ui.util

import android.app.Activity
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.andrii_a.muze.ui.artwork_detail.ArtworkDetailUiState

@Composable
actual fun AnimatedContentScope.DefaultStatusBarControlSideEffect() {
    val view = LocalView.current
    val shouldUseDarkIcons = !isSystemInDarkTheme()

    LaunchedEffect(key1 = true) {
        val window = (view.context as Activity).window
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
            shouldUseDarkIcons
    }
}

@Composable
actual fun AnimatedContentScope.ArtworkDetailStatusBarControlSideEffect(state: ArtworkDetailUiState) {
    val shouldUseDarkIcons = !isSystemInDarkTheme()
    val view = LocalView.current

    DisposableEffect(key1 = state) {
        when {
            state.isLoading || state.error != null -> Unit
            else -> {
                val window = (view.context as Activity).window
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                    false
            }
        }

        onDispose {
            when {
                state.isLoading || state.error != null -> Unit
                else -> {
                    val window = (view.context as Activity).window
                    WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                        shouldUseDarkIcons
                }
            }
        }
    }
}
