package com.andrii_a.muze.domain.models

import com.andrii_a.muze.domain.PlatformParcelable
import com.andrii_a.muze.domain.PlatformParcelize

@PlatformParcelize
data class Artwork(
    val id: Int,
    val name: String,
    val year: String?,
    val location: String,
    val image: Image,
    val description: String?,
    val artist: Artist
) : PlatformParcelable
