package com.andrii_a.muze.data.local.repository

import com.andrii_a.muze.data.local.dao.SavedItemsDao
import com.andrii_a.muze.data.util.asEntity
import com.andrii_a.muze.domain.models.SavedItem
import com.andrii_a.muze.domain.repository.SavedItemsRepository
import com.andrii_a.muze.domain.util.SortOrder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalCoroutinesApi::class)
class SavedItemsRepositoryImpl(private val dao: SavedItemsDao) : SavedItemsRepository {

    override fun getSavedItems(sortOrder: SortOrder): Flow<List<SavedItem>> {
        return dao.getSavedItems(sortOrder).flatMapLatest { entityList ->
            flow {
                val savedItems = entityList.map { it.toSavedItem() }
                emit(savedItems)
            }
        }
    }

    override suspend fun getSavedItemByTitle(title: String): SavedItem? {
        return dao.getSavedItemByTitle(title)?.toSavedItem()
    }

    override suspend fun insertItem(item: SavedItem) {
        dao.insert(item.asEntity())
    }

    override suspend fun deleteItem(item: SavedItem) {
        dao.delete(item.asEntity())
    }

    override suspend fun deleteAllItems() {
        dao.deleteAll()
    }
}
