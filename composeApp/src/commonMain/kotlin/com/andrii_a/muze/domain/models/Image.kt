package com.andrii_a.muze.domain.models

import com.andrii_a.muze.domain.PlatformParcelable
import com.andrii_a.muze.domain.PlatformParcelize

@PlatformParcelize
data class Image(
    val width: Int,
    val height: Int,
    val url: String
) : PlatformParcelable
