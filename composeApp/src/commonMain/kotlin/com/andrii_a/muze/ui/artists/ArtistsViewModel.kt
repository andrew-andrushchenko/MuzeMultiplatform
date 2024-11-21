package com.andrii_a.muze.ui.artists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.andrii_a.muze.domain.models.Artist
import com.andrii_a.muze.domain.models.SavedItem
import com.andrii_a.muze.domain.repository.ArtistsRepository
import com.andrii_a.muze.domain.repository.ArtworksRepository
import com.andrii_a.muze.domain.repository.SavedItemsRepository
import com.andrii_a.muze.domain.util.SavedItemType
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class ArtistsViewModel(
    private val artistsRepository: ArtistsRepository,
    private val artworksRepository: ArtworksRepository,
    private val savedItemsRepository: SavedItemsRepository
) : ViewModel() {

    private val _state: MutableStateFlow<ArtistsUiState> = MutableStateFlow(ArtistsUiState())
    val state: StateFlow<ArtistsUiState> = _state.asStateFlow()

    private val navigationChannel = Channel<ArtistsNavigationEvent>()
    val navigationEventsFlow = navigationChannel.receiveAsFlow()

    init {
        onEvent(ArtistsEvent.RequestArtists)
    }

    fun onEvent(event: ArtistsEvent) {
        when (event) {
            is ArtistsEvent.RequestArtists -> {
                viewModelScope.launch {
                    artistsRepository.getAllArtists().cachedIn(viewModelScope)
                        .collect { pagingData ->
                            _state.update {
                                it.copy(artistsPagingData = pagingData)
                            }
                        }
                }
            }

            is ArtistsEvent.SelectArtist -> {
                viewModelScope.launch {
                    val savedItem = savedItemsRepository.getSavedItemByTitle(event.artist.name)

                    val isArtistInSavedItemsList = savedItem != null && savedItem.type == SavedItemType.ARTIST

                    _state.update {
                        it.copy(
                            selectedArtist = event.artist,
                            isSelectedArtistInSavedList = isArtistInSavedItemsList
                        )
                    }
                }

                viewModelScope.launch {
                    artworksRepository.getArtworksByArtist(event.artist.id).cachedIn(viewModelScope)
                        .collect { pagingData ->
                            _state.update {
                                it.copy(artworksByArtistPagingData = pagingData)
                            }
                        }
                }
            }

            is ArtistsEvent.CleanArtistSelection -> {
                _state.update {
                    it.copy(selectedArtist = null)
                }
            }

            is ArtistsEvent.SelectMoreArtworksByArtist -> {
                viewModelScope.launch {
                    navigationChannel.send(ArtistsNavigationEvent.NavigateToArtworksByArtist(event.artist))
                }
            }

            is ArtistsEvent.SelectArtwork -> {
                viewModelScope.launch {
                    navigationChannel.send(ArtistsNavigationEvent.NavigateToArtworkDetail(event.artworkId))
                }
            }

            is ArtistsEvent.SaveArtist -> {
                saveArtist(event.artist)
            }

            is ArtistsEvent.RemoveArtistFromSaved -> {
                removeArtistRomSaved(event.artist)
            }
        }
    }

    private fun saveArtist(artist: Artist) {
        viewModelScope.launch {
            val savedItem = SavedItem(
                title = artist.name,
                imageUrl = artist.portraitImage.url,
                type = SavedItemType.ARTIST,
                targetItemId = artist.id,
                timestampMillis = Clock.System.now().toEpochMilliseconds()
            )

            withContext(NonCancellable) {
                savedItemsRepository.insertItem(savedItem)
            }
        }

        _state.update {
            it.copy(isSelectedArtistInSavedList = true)
        }
    }

    private fun removeArtistRomSaved(artist: Artist) {
        viewModelScope.launch {
            withContext(NonCancellable) {
                val savedItem = savedItemsRepository.getSavedItemByTitle(artist.name) ?: return@withContext
                savedItemsRepository.deleteItem(savedItem)
            }
        }

        _state.update {
            it.copy(isSelectedArtistInSavedList = false)
        }
    }
}