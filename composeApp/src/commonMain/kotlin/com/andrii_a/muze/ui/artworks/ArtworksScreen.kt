package com.andrii_a.muze.ui.artworks

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import app.cash.paging.compose.collectAsLazyPagingItems
import com.andrii_a.muze.ui.common.ArtworksGridContent
import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.artworks
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtworksScreen(
    state: ArtworksUiState,
    onEvent: (ArtworksEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(resource = Res.string.artworks)) },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        val lazyArtworkItems by rememberUpdatedState(newValue = state.artworks.collectAsLazyPagingItems())

        ArtworksGridContent(
            artworkItems = lazyArtworkItems,
            onArtworkClick = { onEvent(ArtworksEvent.SelectArtwork(it)) },
            contentPadding = innerPadding
        )
    }
}