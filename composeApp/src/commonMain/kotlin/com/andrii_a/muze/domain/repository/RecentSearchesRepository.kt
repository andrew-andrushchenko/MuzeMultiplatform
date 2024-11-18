package com.andrii_a.muze.domain.repository

import com.andrii_a.muze.domain.models.RecentSearchItem
import kotlinx.coroutines.flow.Flow

interface RecentSearchesRepository {

    fun getAllRecentSearches(): Flow<List<RecentSearchItem>>

    suspend fun getRecentSearchByTitle(title: String): RecentSearchItem?

    suspend fun insertItem(item: RecentSearchItem)

    suspend fun updateItem(item: RecentSearchItem)

    suspend fun deleteItem(item: RecentSearchItem)

    suspend fun deleteAllItems()

}