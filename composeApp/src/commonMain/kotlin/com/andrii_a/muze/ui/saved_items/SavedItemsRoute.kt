package com.andrii_a.muze.ui.saved_items

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
import com.andrii_a.muze.domain.util.SavedItemType
import com.andrii_a.muze.ui.navigation.Screen
import com.andrii_a.muze.ui.util.collectAsOneTimeEvents
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.savedItemsRoute(navController: NavController) {
    composable<Screen.Saved>(
        enterTransition = {
            fadeIn(
                animationSpec = tween(300, easing = LinearEasing)
            ) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = spring(stiffness = Spring.StiffnessMedium)
            )
        },
        popEnterTransition = {
            fadeIn(
                animationSpec = tween(300, easing = LinearEasing)
            ) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Up,
                animationSpec = spring(stiffness = Spring.StiffnessMedium)
            )
        }
    ) {
        val viewModel: SavedItemsViewModel = koinViewModel()

        val state by viewModel.state.collectAsStateWithLifecycle()

        viewModel.navigationEventsFlow.collectAsOneTimeEvents { event ->
            when (event) {
                is SelectedItemNavigationEvent.NavigateToSavedItemDetail -> {
                    val savedItem = event.savedItem

                    when (savedItem.type) {
                        SavedItemType.ARTIST -> {
                            navController.navigate(Screen.ArtistDetail(savedItem.targetItemId))
                        }

                        SavedItemType.ARTWORK -> {
                            navController.navigate(Screen.ArtworkDetail(savedItem.targetItemId))
                        }
                    }
                }
            }
        }

        SavedItemsScreen(
            state = state,
            onEvent = viewModel::onEvent
        )
    }
}