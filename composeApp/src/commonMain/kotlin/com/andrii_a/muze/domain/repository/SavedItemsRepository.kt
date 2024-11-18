package com.andrii_a.muze.domain.repository

import com.andrii_a.muze.domain.models.SavedItem
import com.andrii_a.muze.domain.util.SortOrder
import kotlinx.coroutines.flow.Flow

interface SavedItemsRepository {

    fun getSavedItems(sortOrder: SortOrder = SortOrder.BY_DATE_SAVED): Flow<List<SavedItem>>

    suspend fun getSavedItemByTitle(title: String): SavedItem?

    suspend fun insertItem(item: SavedItem)

    suspend fun deleteItem(item: SavedItem)

    suspend fun deleteAllItems()
}