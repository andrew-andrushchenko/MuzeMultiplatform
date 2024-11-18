package com.andrii_a.muze.domain.models

import com.andrii_a.muze.domain.PlatformParcelable
import com.andrii_a.muze.domain.PlatformParcelize

@PlatformParcelize
data class Artist(
    val id: Int,
    val name: String,
    val bornDateString: String?,
    val diedDateString: String?,
    val portraitImage: Image,
    val bio: String
) : PlatformParcelable
