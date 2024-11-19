package com.andrii_a.muze.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andrii_a.muze.domain.models.RecentSearchItem

@Entity("recent_searches_table")
data class RecentSearchItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val timestampMillis: Long
) {
    fun toRecentSearchItem(): RecentSearchItem {
        return RecentSearchItem(
            id = id,
            title = title,
            timestampMillis = timestampMillis
        )
    }
}
