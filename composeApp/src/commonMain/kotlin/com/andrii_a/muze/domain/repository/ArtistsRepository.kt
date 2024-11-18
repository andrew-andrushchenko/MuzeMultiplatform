package com.andrii_a.muze.domain.repository

import app.cash.paging.PagingData
import com.andrii_a.muze.domain.util.Resource
import com.andrii_a.muze.domain.models.Artist
import kotlinx.coroutines.flow.Flow

interface ArtistsRepository {

    fun getAllArtists(): Flow<PagingData<Artist>>

    fun getArtist(id: Int): Flow<Resource<Artist>>

}