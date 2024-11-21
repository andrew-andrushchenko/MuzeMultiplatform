package com.andrii_a.muze.ui.artworks

sealed interface ArtworksEvent {
    data object RequestArtworks : ArtworksEvent

    data class SelectArtwork(val artworkId: Int) : ArtworksEvent

}