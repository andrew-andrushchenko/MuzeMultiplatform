package com.andrii_a.muze.data.remote.repository

import app.cash.paging.Pager
import app.cash.paging.PagingConfig
import app.cash.paging.PagingData
import com.andrii_a.muze.data.remote.service.ArtworksService
import com.andrii_a.muze.data.remote.source.ArtworksByArtistPagingSource
import com.andrii_a.muze.data.remote.source.ArtworksPagingSource
import com.andrii_a.muze.data.util.PAGE_SIZE
import com.andrii_a.muze.domain.models.Artwork
import com.andrii_a.muze.domain.repository.ArtworksRepository
import com.andrii_a.muze.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ArtworksRepositoryImpl(private val artworksService: ArtworksService) : ArtworksRepository {

    override fun getAllArtworks(): Flow<PagingData<Artwork>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ArtworksPagingSource(artworksService) }
        ).flow

    override fun getArtworksByArtist(artistId: Int): Flow<PagingData<Artwork>> =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { ArtworksByArtistPagingSource(artistId, artworksService) }
        ).flow

    override fun getArtwork(id: Int): Flow<Resource<Artwork>> = flow {
        emit(Resource.Loading)

        when (val result = artworksService.getArtwork(id)) {
            is Resource.Error -> emit(Resource.Error(result.code, result.reason))
            is Resource.Success -> emit(Resource.Success(result.value.toArtwork()))
            else -> Unit
        }
    }
}