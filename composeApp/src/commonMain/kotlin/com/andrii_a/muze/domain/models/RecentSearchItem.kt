package com.andrii_a.muze.domain.models

data class RecentSearchItem(
    val id: Int = 0,
    val title: String,
    val timestampMillis: Long
)