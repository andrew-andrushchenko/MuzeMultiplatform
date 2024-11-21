package com.andrii_a.muze.ui.artworks_by_artist

sealed interface ArtworksByArtistEvent {
    data class RequestArtworks(val artistId: Int) : ArtworksByArtistEvent

    data class SelectArtwork(val artworkId: Int) : ArtworksByArtistEvent

    data object GoBack : ArtworksByArtistEvent
}