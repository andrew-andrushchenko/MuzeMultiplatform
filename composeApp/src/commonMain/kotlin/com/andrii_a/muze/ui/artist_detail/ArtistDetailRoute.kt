package com.andrii_a.muze.ui.artist_detail

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
import com.andrii_a.muze.ui.util.DefaultStatusBarControlSideEffect
import com.andrii_a.muze.ui.util.collectAsOneTimeEvents
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.artistDetailRoute(navController: NavController) {
    composable<Screen.ArtistDetail>(
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
        DefaultStatusBarControlSideEffect()

        val viewModel: ArtistDetailViewModel = koinViewModel()
        val state by viewModel.state.collectAsStateWithLifecycle()

        viewModel.navigationEventsFlow.collectAsOneTimeEvents { event ->
            when (event) {
                is ArtistDetailNavigationEvent.NavigateBack -> {
                    navController.navigateUp()
                }

                is ArtistDetailNavigationEvent.NavigateToArtistArtworks -> {
                    navController.navigate(Screen.ArtworksByArtist(event.artistId, ""))
                }

                is ArtistDetailNavigationEvent.NavigateToArtworkDetail -> {
                    navController.navigate(Screen.ArtworkDetail(event.artworkId))
                }
            }
        }

        ArtistDetailScreen(
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}
