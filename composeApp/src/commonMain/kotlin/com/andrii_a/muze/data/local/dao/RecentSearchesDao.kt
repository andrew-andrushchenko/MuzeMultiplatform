package com.andrii_a.muze.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.andrii_a.muze.data.local.entity.RecentSearchItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentSearchesDao {
    @Query("SELECT * FROM recent_searches_table ORDER BY timestampMillis DESC")
    fun getRecentSearches(): Flow<List<RecentSearchItemEntity>>

    @Query("SELECT * FROM recent_searches_table WHERE title = :title LIMIT 1")
    suspend fun getRecentSearchByTitle(title: String) : RecentSearchItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recentSearchItemEntity: RecentSearchItemEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(recentSearchItemEntity: RecentSearchItemEntity)

    @Delete
    suspend fun delete(recentSearchItemEntity: RecentSearchItemEntity)

    @Query("DELETE FROM recent_searches_table")
    suspend fun deleteAll()
}