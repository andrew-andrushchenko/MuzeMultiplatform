package com.andrii_a.muze.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andrii_a.muze.data.local.entity.SavedItemEntity
import com.andrii_a.muze.domain.util.SortOrder
import kotlinx.coroutines.flow.Flow

@Dao
interface SavedItemsDao {

    fun getSavedItems(order: SortOrder): Flow<List<SavedItemEntity>> {
        return when (order) {
            SortOrder.BY_TITLE -> {
                getSavedItemsOrderedByTitle()
            }
            SortOrder.BY_DATE_SAVED -> {
                getSavedItemsOrderedByTimeSaved()
            }
        }
    }

    @Query("SELECT * FROM saved_items_table ORDER BY title ASC")
    fun getSavedItemsOrderedByTitle(): Flow<List<SavedItemEntity>>

    @Query("SELECT * FROM saved_items_table ORDER BY timestampMillis DESC")
    fun getSavedItemsOrderedByTimeSaved(): Flow<List<SavedItemEntity>>

    @Query("SELECT * FROM saved_items_table WHERE title = :title LIMIT 1")
    suspend fun getSavedItemByTitle(title: String): SavedItemEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(savedItemEntity: SavedItemEntity)

    @Delete
    suspend fun delete(savedItemEntity: SavedItemEntity)

    @Query("DELETE FROM saved_items_table")
    suspend fun deleteAll()

}
