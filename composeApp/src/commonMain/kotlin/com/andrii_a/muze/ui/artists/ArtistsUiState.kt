package com.andrii_a.muze.ui.artists

import androidx.compose.runtime.Stable
import androidx.paging.PagingData
import com.andrii_a.muze.domain.models.Artist
import com.andrii_a.muze.domain.models.Artwork
import com.andrii_a.muze.ui.util.emptyPagingData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Stable
data class ArtistsUiState(
    private val artistsPagingData: PagingData<Artist> = emptyPagingData(),
    private val artworksByArtistPagingData: PagingData<Artwork> = emptyPagingData(),
    val selectedArtist: Artist? = null,
    val isSelectedArtistInSavedList: Boolean = false
) {
    private val _artists: MutableStateFlow<PagingData<Artist>> = MutableStateFlow(emptyPagingData())
    val artists: StateFlow<PagingData<Artist>> = _artists.asStateFlow()

    private val _artworksByArtist: MutableStateFlow<PagingData<Artwork>> = MutableStateFlow(emptyPagingData())
    val artworksByArtist: StateFlow<PagingData<Artwork>> = _artworksByArtist.asStateFlow()

    init {
        _artists.update { artistsPagingData }
        _artworksByArtist.update { artworksByArtistPagingData }
    }
}