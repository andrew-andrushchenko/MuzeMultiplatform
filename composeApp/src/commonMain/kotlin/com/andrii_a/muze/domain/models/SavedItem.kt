package com.andrii_a.muze.domain.models

import com.andrii_a.muze.domain.PlatformParcelable
import com.andrii_a.muze.domain.PlatformParcelize
import com.andrii_a.muze.domain.util.SavedItemType

@PlatformParcelize
data class SavedItem(
    val id: Int = 0,
    val title: String,
    val imageUrl: String,
    val type: SavedItemType,
    val targetItemId: Int,
    val timestampMillis: Long
) : PlatformParcelable
