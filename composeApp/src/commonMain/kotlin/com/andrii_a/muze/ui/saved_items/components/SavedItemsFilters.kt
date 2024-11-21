package com.andrii_a.muze.ui.saved_items.components

import com.andrii_a.muze.domain.util.SortOrder

data class SavedItemsFilters(
    val sortOrder: SortOrder = SortOrder.BY_DATE_SAVED,
    val displayItemTypes: DisplayItemTypes = DisplayItemTypes.ALL
)
