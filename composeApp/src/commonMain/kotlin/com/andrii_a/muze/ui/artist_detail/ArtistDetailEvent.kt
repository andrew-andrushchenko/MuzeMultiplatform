package com.andrii_a.muze.ui.artist_detail

import com.andrii_a.muze.domain.models.Artist

sealed interface ArtistDetailEvent {
    data class RequestArtist(val artistId: Int) : ArtistDetailEvent

    data class SelectArtwork(val artworkId: Int) : ArtistDetailEvent

    data class SelectMoreArtworks(val artistId: Int) : ArtistDetailEvent

    data object GoBack : ArtistDetailEvent

    data class SaveArtist(val artist: Artist) : ArtistDetailEvent

    data class RemoveArtistFromSaved(val artist: Artist) : ArtistDetailEvent
}