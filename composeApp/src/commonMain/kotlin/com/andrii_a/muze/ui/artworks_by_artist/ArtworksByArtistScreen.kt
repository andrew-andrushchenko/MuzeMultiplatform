package com.andrii_a.muze.ui.artworks_by_artist

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import app.cash.paging.compose.collectAsLazyPagingItems
import com.andrii_a.muze.ui.common.ArtworksGridContent
import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.artworks_by_formatted
import muzemultiplatform.composeapp.generated.resources.navigate_back
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtworksByArtistScreen(
    state: ArtworksByArtistUiState,
    onEvent: (ArtworksByArtistEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(
                            resource = Res.string.artworks_by_formatted,
                            state.artistName
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onEvent(ArtworksByArtistEvent.GoBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(resource = Res.string.navigate_back)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        val lazyArtworkItems by rememberUpdatedState(newValue = state.artworks.collectAsLazyPagingItems())

        ArtworksGridContent(
            artworkItems = lazyArtworkItems,
            onArtworkClick = { onEvent(ArtworksByArtistEvent.SelectArtwork(it)) },
            contentPadding = innerPadding
        )
    }
}