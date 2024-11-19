package com.andrii_a.muze.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andrii_a.muze.domain.models.SavedItem
import com.andrii_a.muze.domain.util.SavedItemType

@Entity(tableName = "saved_items_table")
data class SavedItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val imageUrl: String,
    val type: SavedItemType,
    val targetItemId: Int,
    val timestampMillis: Long
) {
    fun toSavedItem(): SavedItem {
        return SavedItem(
            id = id,
            title = title,
            imageUrl = imageUrl,
            type = type,
            targetItemId = targetItemId,
            timestampMillis = timestampMillis
        )
    }
}
