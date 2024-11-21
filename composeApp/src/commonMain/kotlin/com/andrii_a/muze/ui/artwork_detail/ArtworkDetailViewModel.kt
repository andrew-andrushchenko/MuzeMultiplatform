package com.andrii_a.muze.ui.artwork_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.andrii_a.muze.domain.models.Artwork
import com.andrii_a.muze.domain.models.SavedItem
import com.andrii_a.muze.domain.util.Resource
import com.andrii_a.muze.domain.repository.ArtworksRepository
import com.andrii_a.muze.domain.repository.SavedItemsRepository
import com.andrii_a.muze.domain.util.SavedItemType
import com.andrii_a.muze.ui.common.UiErrorWithRetry
import com.andrii_a.muze.ui.common.UiText
import com.andrii_a.muze.ui.navigation.Screen
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class ArtworkDetailViewModel(
    private val artworksRepository: ArtworksRepository,
    private val savedItemsRepository: SavedItemsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state: MutableStateFlow<ArtworkDetailUiState> = MutableStateFlow(
        ArtworkDetailUiState()
    )
    val state: StateFlow<ArtworkDetailUiState> = _state.asStateFlow()

    private val navigationChannel = Channel<ArtworkDetailNavigationEvent>()
    val navigationEventsFlow = navigationChannel.receiveAsFlow()

    init {
        val artworkId = savedStateHandle.toRoute<Screen.ArtworkDetail>().artworkId
        onEvent(ArtworkDetailEvent.RequestArtwork(artworkId))
    }

    fun onEvent(event: ArtworkDetailEvent) {
        when (event) {
            is ArtworkDetailEvent.RequestArtwork -> {
                getArtwork(event.artworkId)
            }

            is ArtworkDetailEvent.ShowInfoDialog -> {
                _state.update { it.copy(isInfoDialogOpened = true) }
            }

            is ArtworkDetailEvent.DismissInfoDialog -> {
                _state.update { it.copy(isInfoDialogOpened = false) }
            }

            is ArtworkDetailEvent.GoBack -> {
                viewModelScope.launch {
                    navigationChannel.send(ArtworkDetailNavigationEvent.NavigateBack)
                }
            }

            is ArtworkDetailEvent.SaveArtWork -> {
                saveArtwork(event.artwork)
            }

            is ArtworkDetailEvent.RemoveArtworkFromSaved -> {
                removeArtworkFromSaved(event.artwork)
            }
        }
    }

    private fun getArtwork(artworkId: Int) {
        artworksRepository.getArtwork(artworkId).onEach { result ->
            when (result) {
                is Resource.Empty -> Unit

                is Resource.Loading -> {
                    _state.update { it.copy(isLoading = true) }
                }

                is Resource.Error -> {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = UiErrorWithRetry(
                                reason = UiText.DynamicString(result.reason.orEmpty()),
                                onRetry = {
                                    onEvent(
                                        ArtworkDetailEvent.RequestArtwork(artworkId)
                                    )
                                }
                            )
                        )
                    }
                }

                is Resource.Success -> {
                    val artwork = result.value

                    val savedItem = savedItemsRepository.getSavedItemByTitle(artwork.name)

                    val isArworkInSavedItemsList = savedItem != null && savedItem.type == SavedItemType.ARTWORK

                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            artwork = artwork,
                            isArtworkInSavedList = isArworkInSavedItemsList
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun saveArtwork(artwork: Artwork) {
        viewModelScope.launch {
            val savedItem = SavedItem(
                title = artwork.name,
                imageUrl = artwork.image.url,
                type = SavedItemType.ARTWORK,
                targetItemId = artwork.id,
                timestampMillis = Clock.System.now().toEpochMilliseconds()
            )

            withContext(NonCancellable) {
                savedItemsRepository.insertItem(savedItem)
            }
        }

        _state.update {
            it.copy(isArtworkInSavedList = true)
        }
    }

    private fun removeArtworkFromSaved(artwork: Artwork) {
        viewModelScope.launch {
            val savedItem = savedItemsRepository.getSavedItemByTitle(artwork.name) ?: return@launch

            withContext(NonCancellable) {
                savedItemsRepository.deleteItem(savedItem)
            }
        }

        _state.update {
            it.copy(isArtworkInSavedList = false)
        }
    }
}