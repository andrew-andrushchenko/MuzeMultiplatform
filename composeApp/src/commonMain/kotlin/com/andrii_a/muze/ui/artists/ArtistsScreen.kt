package com.andrii_a.muze.ui.artists

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import com.andrii_a.muze.domain.models.Artist
import com.andrii_a.muze.ui.common.EmptyContentBanner
import com.andrii_a.muze.ui.common.PlatformBackHandler
import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.select_artist_to_see_details
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ArtistsScreen(
    state: ArtistsUiState,
    onEvent: (ArtistsEvent) -> Unit,
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Artist>()

    PlatformBackHandler(
        enabled = navigator.canNavigateBack(),
        onNavigateBack = { navigator.navigateBack() }
    )

    ListDetailPaneScaffold(
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane(
                modifier = Modifier.preferredWidth(350.dp)
            ) {
                val lazyArtistItems by rememberUpdatedState(newValue = state.artists.collectAsLazyPagingItems())

                ArtistListPane(
                    lazyArtistItems = lazyArtistItems,
                    selectedArtist = state.selectedArtist,
                    onArtistClick = { artist ->
                        onEvent(ArtistsEvent.SelectArtist(artist))
                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, artist)
                    }
                )
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let { artist ->
                    val lazyArtworkItems = state.artworksByArtist.collectAsLazyPagingItems()

                    ArtistDetailPane(
                        artist = artist,
                        lazyArtworkItems = lazyArtworkItems,
                        isArtistInSavedList = state.isSelectedArtistInSavedList,
                        navigateBack = {
                            onEvent(ArtistsEvent.CleanArtistSelection)
                            navigator.navigateTo(ListDetailPaneScaffoldRole.List)
                        },
                        onSaveArtist = { onEvent(ArtistsEvent.SaveArtist(it)) },
                        onRemoveArtistFromSaved = { onEvent(ArtistsEvent.RemoveArtistFromSaved(it)) },
                        onArtworkSelect = { id -> onEvent(ArtistsEvent.SelectArtwork(id)) },
                        onMoreArtworksClick = {
                            onEvent(
                                ArtistsEvent.SelectMoreArtworksByArtist(
                                    artist
                                )
                            )
                        },
                        modifier = Modifier.padding(8.dp)
                    )
                } ?: EmptyContentBanner(
                    imageVector = null,
                    message = stringResource(Res.string.select_artist_to_see_details)
                )
            }
        }
    )
}
