package com.andrii_a.muze.ui.search

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.traversalIndex
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.collectAsLazyPagingItems
import com.andrii_a.muze.ui.common.ArtistsListContent
import com.andrii_a.muze.ui.common.ArtworksGridContent
import kotlinx.coroutines.launch
import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.artists
import muzemultiplatform.composeapp.generated.resources.artworks
import muzemultiplatform.composeapp.generated.resources.navigate_back
import muzemultiplatform.composeapp.generated.resources.recent_searches
import muzemultiplatform.composeapp.generated.resources.search_icon
import muzemultiplatform.composeapp.generated.resources.type_artist_or_artwork_name
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchUiState,
    onEvent: (SearchEvent) -> Unit
) {
    val pageState = rememberPagerState(initialPage = 0) { SearchScreenTabs.entries.size }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .semantics { isTraversalGroup = true }
    ) {
        var text by remember { mutableStateOf(state.query) }
        var searchBoxExpanded by rememberSaveable { mutableStateOf(false) }

        Column {
            SearchBar(
                inputField = {
                    SearchBarDefaults.InputField(
                        query = text,
                        onQueryChange = { text = it },
                        onSearch = {
                            searchBoxExpanded = false
                            onEvent(SearchEvent.PerformSearch(query = text))
                        },
                        expanded = searchBoxExpanded,
                        onExpandedChange = { searchBoxExpanded = it },
                        placeholder = { Text(text = stringResource(resource = Res.string.type_artist_or_artwork_name)) },
                        leadingIcon = {
                            AnimatedContent(
                                targetState = searchBoxExpanded,
                                label = ""
                            ) { isExpanded ->
                                if (isExpanded) {
                                    IconButton(onClick = { searchBoxExpanded = false }) {
                                        Icon(
                                            Icons.AutoMirrored.Default.ArrowBack,
                                            contentDescription = stringResource(resource = Res.string.navigate_back)
                                        )
                                    }
                                } else {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = stringResource(resource = Res.string.search_icon)
                                    )
                                }
                            }
                        },
                    )
                },
                expanded = searchBoxExpanded,
                onExpandedChange = { searchBoxExpanded = it },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .semantics { traversalIndex = -1f },
            ) {
                Text(
                    text = stringResource(Res.string.recent_searches),
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                RecentSearchesList(
                    recentSearches = state.recentSearches,
                    onItemSelected = { item ->
                        searchBoxExpanded = false
                        text = item.title
                        onEvent(SearchEvent.PerformSearch(query = text))
                    },
                    onDeleteItem = { item ->
                        onEvent(SearchEvent.DeleteRecentSearchItem(item))
                    },
                    onDeleteAllItems = {
                        onEvent(SearchEvent.DeleteAllRecentSearches)
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            SearchTabs(
                pagerState = pageState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(450.dp)
                    .widthIn(min = 200.dp, max = 600.dp)
                    .padding(horizontal = 16.dp)
            )

            SearchPages(
                pagerState = pageState,
                uiState = state,
                onEvent = onEvent
            )
        }
    }
}

@Composable
private fun SearchTabs(
    pagerState: PagerState,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()

    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        val options = SearchScreenTabs.entries

        options.forEachIndexed { index, tabPage ->
            SegmentedButton(
                selected = index == pagerState.currentPage,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                label = {
                    Text(
                        text = stringResource(resource = tabPage.titleRes),
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            )
        }
    }
}

@Composable
private fun SearchPages(
    pagerState: PagerState,
    uiState: SearchUiState,
    onEvent: (SearchEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyArtistItems by rememberUpdatedState(newValue = uiState.artists.collectAsLazyPagingItems())
    val lazyArtworkItems by rememberUpdatedState(newValue = uiState.artworks.collectAsLazyPagingItems())

    HorizontalPager(
        state = pagerState,
        modifier = modifier
    ) { pageIndex ->
        when (pageIndex) {
            SearchScreenTabs.Artists.ordinal -> {
                val listState = rememberLazyListState()

                ArtistsListContent(
                    artistItems = lazyArtistItems,
                    onArtistClick = { onEvent(SearchEvent.SelectArtist(it)) },
                    listState = listState,
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        bottom = WindowInsets.systemBars.asPaddingValues()
                            .calculateBottomPadding() + 150.dp,
                    ),
                    scrollToTopButtonPadding = PaddingValues(
                        bottom = WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding() + 90.dp
                    )
                )
            }

            SearchScreenTabs.Artworks.ordinal -> {
                val gridState = rememberLazyStaggeredGridState()

                ArtworksGridContent(
                    artworkItems = lazyArtworkItems,
                    gridState = gridState,
                    onArtworkClick = { onEvent(SearchEvent.SelectArtwork(it)) },
                    contentPadding = PaddingValues(
                        top = 16.dp,
                        start = 16.dp,
                        end = 16.dp,
                        bottom = WindowInsets.systemBars.asPaddingValues()
                            .calculateBottomPadding() + 150.dp,
                    ),
                    scrollToTopButtonPadding = PaddingValues(
                        bottom = WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding() + 90.dp
                    )
                )
            }

            else -> throw IllegalStateException("Tab screen was not declared!")
        }
    }
}

private enum class SearchScreenTabs(val titleRes: StringResource) {
    Artists(Res.string.artists),
    Artworks(Res.string.artworks),
}

