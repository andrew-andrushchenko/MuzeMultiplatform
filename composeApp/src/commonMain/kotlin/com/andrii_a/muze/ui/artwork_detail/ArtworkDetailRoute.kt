package com.andrii_a.muze.ui.artwork_detail

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.andrii_a.muze.ui.navigation.Screen
import com.andrii_a.muze.ui.util.ArtworkDetailStatusBarControlSideEffect
import com.andrii_a.muze.ui.util.collectAsOneTimeEvents
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.artworkDetailRoute(navController: NavController) {
    composable<Screen.ArtworkDetail>(
        enterTransition = {
            fadeIn(
                animationSpec = tween(300, easing = LinearEasing)
            ) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = spring(stiffness = Spring.StiffnessMedium)
            )
        },
        popEnterTransition = {
            fadeIn(
                animationSpec = tween(300, easing = LinearEasing)
            ) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = spring(stiffness = Spring.StiffnessMedium)
            )
        },
    ) {
        val viewModel: ArtworkDetailViewModel = koinViewModel()

        val state by viewModel.state.collectAsStateWithLifecycle()

        ArtworkDetailStatusBarControlSideEffect(state)

        viewModel.navigationEventsFlow.collectAsOneTimeEvents { event ->
            when (event) {
                ArtworkDetailNavigationEvent.NavigateBack -> navController.navigateUp()
            }
        }

        ArtworkDetailScreen(
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}
