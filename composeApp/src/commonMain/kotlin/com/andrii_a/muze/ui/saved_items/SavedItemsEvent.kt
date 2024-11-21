package com.andrii_a.muze.ui.saved_items

import com.andrii_a.muze.domain.models.SavedItem
import com.andrii_a.muze.ui.saved_items.components.SavedItemsFilters

sealed interface SavedItemsEvent {

    data class ChangeFilters(val filters: SavedItemsFilters) : SavedItemsEvent

    data class SelectItem(val item: SavedItem) : SavedItemsEvent

    data class RemoveItem(val item: SavedItem) : SavedItemsEvent

    data object RemoveAllItems : SavedItemsEvent

    data object OpenFilterDialog : SavedItemsEvent

    data object DismissFilterDialog : SavedItemsEvent
}