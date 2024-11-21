package com.andrii_a.muze.ui.artwork_detail

import com.andrii_a.muze.domain.models.Artwork

sealed interface ArtworkDetailEvent {
    data class RequestArtwork(val artworkId: Int) : ArtworkDetailEvent

    data object ShowInfoDialog : ArtworkDetailEvent

    data object DismissInfoDialog : ArtworkDetailEvent

    data object GoBack : ArtworkDetailEvent

    data class SaveArtWork(val artwork: Artwork) : ArtworkDetailEvent

    data class RemoveArtworkFromSaved(val artwork: Artwork) : ArtworkDetailEvent
}