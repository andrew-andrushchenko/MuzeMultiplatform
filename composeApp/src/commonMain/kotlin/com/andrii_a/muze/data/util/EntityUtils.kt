package com.andrii_a.muze.data.util

import com.andrii_a.muze.data.local.entity.RecentSearchItemEntity
import com.andrii_a.muze.data.local.entity.SavedItemEntity
import com.andrii_a.muze.domain.models.RecentSearchItem
import com.andrii_a.muze.domain.models.SavedItem

fun SavedItem.asEntity(): SavedItemEntity {
    return SavedItemEntity(
        id = this.id,
        title = this.title,
        imageUrl = this.imageUrl,
        type = this.type,
        targetItemId = targetItemId,
        timestampMillis = this.timestampMillis
    )
}

fun RecentSearchItem.asEntity(): RecentSearchItemEntity {
    return RecentSearchItemEntity(
        id = this.id,
        title = this.title,
        timestampMillis = this.timestampMillis
    )
}