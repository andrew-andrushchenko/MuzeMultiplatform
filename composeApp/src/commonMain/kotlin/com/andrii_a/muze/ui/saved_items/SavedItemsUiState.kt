package com.andrii_a.muze.ui.saved_items

import com.andrii_a.muze.domain.models.SavedItem
import com.andrii_a.muze.ui.saved_items.components.SavedItemsFilters

data class SavedItemsUiState(
    val items: List<SavedItem> = emptyList(),
    val filters: SavedItemsFilters = SavedItemsFilters(),
    val isFilterDialogOpen: Boolean = false
)
