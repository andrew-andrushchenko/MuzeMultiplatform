package com.andrii_a.muze.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.andrii_a.muze.domain.models.RecentSearchItem
import com.andrii_a.muze.domain.repository.RecentSearchesRepository
import com.andrii_a.muze.domain.repository.SearchRepository
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class SearchViewModel(
    private val searchRepository: SearchRepository,
    private val recentSearchesRepository: RecentSearchesRepository
) : ViewModel() {

    private val _state: MutableStateFlow<SearchUiState> = MutableStateFlow(SearchUiState())
    val state = combine(
        recentSearchesRepository.getAllRecentSearches(),
        _state
    ) { recentSearches, state ->
        state.copy(recentSearches = recentSearches)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = _state.value
    )

    private val navigationChannel = Channel<SearchNavigationEvent>()
    val navigationEventsFlow = navigationChannel.receiveAsFlow()

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.PerformSearch -> {
                performSearch(event.query)
            }

            is SearchEvent.SelectArtist -> {
                viewModelScope.launch {
                    navigationChannel.send(SearchNavigationEvent.NavigateToArtistDetail(event.artist.id))
                }
            }

            is SearchEvent.SelectArtwork -> {
                viewModelScope.launch {
                    navigationChannel.send(SearchNavigationEvent.NavigateToArtworkDetail(event.artworkId))
                }
            }

            is SearchEvent.DeleteRecentSearchItem -> {
                deleteRecentSearch(event.item)
            }

            is SearchEvent.DeleteAllRecentSearches -> {
                deleteAllRecentSearches()
            }
        }
    }

    private fun performSearch(query: String) {
        saveRecentQuery(query)

        val artistsFlow = searchRepository.searchArtists(query).cachedIn(viewModelScope)
        val artworksFlow = searchRepository.searchArtworks(query).cachedIn(viewModelScope)

        combine(artistsFlow, artworksFlow) { artistsPagingData, artworksPagingData ->
            _state.update {
                it.copy(
                    query = query,
                    artistsPagingData = artistsPagingData,
                    artworksPagingData = artworksPagingData
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun saveRecentQuery(query: String) {
        if (query.isEmpty()) return

        viewModelScope.launch {
            withContext(NonCancellable) {
                val itemToModify = recentSearchesRepository.getRecentSearchByTitle(title = query)
                itemToModify?.let {
                    recentSearchesRepository.updateItem(
                        it.copy(timestampMillis = Clock.System.now().toEpochMilliseconds())
                    )
                } ?: run {
                    val newRecentSearchItem = RecentSearchItem(
                        title = query,
                        timestampMillis = Clock.System.now().toEpochMilliseconds()
                    )

                    recentSearchesRepository.insertItem(newRecentSearchItem)
                }
            }
        }
    }

    private fun deleteRecentSearch(item: RecentSearchItem) {
        viewModelScope.launch {
            withContext(NonCancellable) {
                recentSearchesRepository.deleteItem(item)
            }
        }
    }

    private fun deleteAllRecentSearches() {
        viewModelScope.launch {
            withContext(NonCancellable) {
                recentSearchesRepository.deleteAllItems()
            }
        }
    }
}