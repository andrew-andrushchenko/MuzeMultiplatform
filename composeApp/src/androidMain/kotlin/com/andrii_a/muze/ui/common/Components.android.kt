package com.andrii_a.muze.ui.common

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onNavigateBack: () -> Unit) {
    BackHandler(
        enabled = enabled,
        onBack = onNavigateBack
    )
}