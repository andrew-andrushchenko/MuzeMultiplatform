package com.andrii_a.muze.ui.artist_detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import com.andrii_a.muze.ui.artists.ArtistDetailPane
import com.andrii_a.muze.ui.common.ErrorBanner
import com.andrii_a.muze.ui.common.UiErrorWithRetry
import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.navigate_back
import org.jetbrains.compose.resources.stringResource

@Composable
fun ArtistDetailScreen(
    state: ArtistDetailUiState,
    onEvent: (ArtistDetailEvent) -> Unit
) {
    when {
        state.isLoading -> {
            LoadingStateContent(
                onNavigateBack = { onEvent(ArtistDetailEvent.GoBack) }
            )
        }

        !state.isLoading && state.error == null && state.artist != null -> {
            SuccessStateContent(
                state = state,
                onEvent = onEvent
            )
        }

        else -> {
            val error = state.error as? UiErrorWithRetry
            //Toast.makeText(LocalContext.current, error?.reason?.asString(), Toast.LENGTH_SHORT).show()

            ErrorStateContent(
                onRetry = {
                    error?.onRetry?.invoke()
                },
                onNavigateBack = { onEvent(ArtistDetailEvent.GoBack) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoadingStateContent(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(resource = Res.string.navigate_back),
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ErrorStateContent(
    onRetry: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(resource = Res.string.navigate_back),
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ErrorBanner(
            onRetry = onRetry,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessStateContent(
    state: ArtistDetailUiState,
    onEvent: (ArtistDetailEvent) -> Unit
) {
    val artist = state.artist!!

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = artist.name) },
                navigationIcon = {
                    IconButton(onClick = { onEvent(ArtistDetailEvent.GoBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(resource = Res.string.navigate_back)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (state.isArtistInSavedList) {
                                onEvent(ArtistDetailEvent.RemoveArtistFromSaved(artist))
                            } else {
                                onEvent(ArtistDetailEvent.SaveArtist(artist))
                            }
                        },
                        modifier = Modifier.padding(8.dp).requiredSize(48.dp)
                    ) {
                        Icon(
                            imageVector = if (state.isArtistInSavedList) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                            contentDescription = stringResource(Res.string.navigate_back)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        val lazyArtistItems by rememberUpdatedState(newValue = state.artistArtworks.collectAsLazyPagingItems())

        ArtistDetailPane(
            artist = artist,
            lazyArtworkItems = lazyArtistItems,
            onArtworkSelect = { id -> onEvent(ArtistDetailEvent.SelectArtwork(id)) },
            onMoreArtworksClick = { onEvent(ArtistDetailEvent.SelectMoreArtworks(artist.id)) },
            tonalElevation = 0.dp,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
