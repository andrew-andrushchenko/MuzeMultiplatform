package com.andrii_a.muze.data.remote.service

import com.andrii_a.muze.data.remote.dto.ArtworkDto
import com.andrii_a.muze.domain.util.Resource

interface ArtworksService {

    suspend fun getArtworks(
        page: Int?,
        perPage: Int?
    ): Resource<List<ArtworkDto>>

    suspend fun getArtworksByArtist(
        artistId: Int,
        page: Int?,
        perPage: Int?
    ): Resource<List<ArtworkDto>>

    suspend fun getArtwork(id: Int): Resource<ArtworkDto>
}