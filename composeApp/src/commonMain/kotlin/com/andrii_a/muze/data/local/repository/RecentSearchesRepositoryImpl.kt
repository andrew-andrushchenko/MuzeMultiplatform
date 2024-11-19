package com.andrii_a.muze.data.local.repository

import com.andrii_a.muze.data.local.dao.RecentSearchesDao
import com.andrii_a.muze.data.util.asEntity
import com.andrii_a.muze.domain.models.RecentSearchItem
import com.andrii_a.muze.domain.repository.RecentSearchesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalCoroutinesApi::class)
class RecentSearchesRepositoryImpl(private val dao: RecentSearchesDao) : RecentSearchesRepository {

    override fun getAllRecentSearches(): Flow<List<RecentSearchItem>> {
        return dao.getRecentSearches().flatMapLatest { entityList ->
            flow {
                val recentSearchItems = entityList.map { it.toRecentSearchItem() }
                emit(recentSearchItems)
            }
        }
    }

    override suspend fun getRecentSearchByTitle(title: String): RecentSearchItem? {
        return dao.getRecentSearchByTitle(title)?.toRecentSearchItem()
    }

    override suspend fun insertItem(item: RecentSearchItem) {
        dao.insert(item.asEntity())
    }

    override suspend fun updateItem(item: RecentSearchItem) {
        dao.update(item.asEntity())
    }

    override suspend fun deleteItem(item: RecentSearchItem) {
        dao.delete(item.asEntity())
    }

    override suspend fun deleteAllItems() {
        dao.deleteAll()
    }
}