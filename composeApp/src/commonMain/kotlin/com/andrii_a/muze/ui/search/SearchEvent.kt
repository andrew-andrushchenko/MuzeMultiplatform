package com.andrii_a.muze.ui.search

import com.andrii_a.muze.domain.models.Artist
import com.andrii_a.muze.domain.models.RecentSearchItem

sealed interface SearchEvent {
    data class PerformSearch(val query: String) : SearchEvent

    data class SelectArtist(val artist: Artist) : SearchEvent

    data class SelectArtwork(val artworkId: Int) : SearchEvent

    data class DeleteRecentSearchItem(val item: RecentSearchItem) : SearchEvent

    data object DeleteAllRecentSearches : SearchEvent
}