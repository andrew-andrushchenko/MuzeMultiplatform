package com.andrii_a.muze.ui.saved_items

import com.andrii_a.muze.domain.models.SavedItem

sealed interface SelectedItemNavigationEvent {

    data class NavigateToSavedItemDetail(val savedItem: SavedItem) : SelectedItemNavigationEvent
}