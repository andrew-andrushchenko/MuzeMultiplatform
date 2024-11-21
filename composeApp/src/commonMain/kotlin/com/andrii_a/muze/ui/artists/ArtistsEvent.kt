package com.andrii_a.muze.ui.artists

import com.andrii_a.muze.domain.models.Artist

sealed interface ArtistsEvent {
    data object RequestArtists : ArtistsEvent

    data class SelectArtist(val artist: Artist) : ArtistsEvent

    data object CleanArtistSelection : ArtistsEvent

    data class SelectMoreArtworksByArtist(val artist: Artist) : ArtistsEvent

    data class SelectArtwork(val artworkId: Int) : ArtistsEvent

    data class SaveArtist(val artist: Artist) : ArtistsEvent

    data class RemoveArtistFromSaved(val artist: Artist) : ArtistsEvent
}