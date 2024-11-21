package com.andrii_a.muze.ui.artists

import com.andrii_a.muze.domain.models.Artist

sealed interface ArtistsNavigationEvent {
    data class NavigateToArtworksByArtist(val artist: Artist) : ArtistsNavigationEvent
    data class NavigateToArtworkDetail(val artworkId: Int) : ArtistsNavigationEvent
}