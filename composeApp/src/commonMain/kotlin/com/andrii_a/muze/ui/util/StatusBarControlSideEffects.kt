package com.andrii_a.muze.ui.util

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import com.andrii_a.muze.ui.artwork_detail.ArtworkDetailUiState

@Composable
expect fun AnimatedContentScope.DefaultStatusBarControlSideEffect()

@Composable
expect fun AnimatedContentScope.ArtworkDetailStatusBarControlSideEffect(state: ArtworkDetailUiState)