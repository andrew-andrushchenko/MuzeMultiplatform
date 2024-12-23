package com.andrii_a.muze.data.remote.source

import com.andrii_a.muze.data.remote.base.BasePagingSource
import com.andrii_a.muze.data.remote.service.ArtworksService
import com.andrii_a.muze.data.util.INITIAL_PAGE_INDEX
import com.andrii_a.muze.data.util.PAGE_SIZE
import com.andrii_a.muze.domain.models.Artwork
import com.andrii_a.muze.domain.util.Resource
import kotlinx.coroutines.ensureActive
import kotlin.coroutines.coroutineContext

class ArtworksPagingSource(private val artworksService: ArtworksService) : BasePagingSource<Artwork>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Artwork> {
        val pageKey = params.key ?: INITIAL_PAGE_INDEX

        return try {
            val result = artworksService.getArtworks(
                page = pageKey,
                perPage = PAGE_SIZE
            )

            val artworks: List<Artwork> = when (result) {
                is Resource.Empty, Resource.Loading -> emptyList()
                is Resource.Error -> throw result.asException()
                is Resource.Success -> result.value.map { it.toArtwork() }
            }

            LoadResult.Page(
                data = artworks,
                prevKey = if (pageKey == INITIAL_PAGE_INDEX) null else pageKey - 1,
                nextKey = if (artworks.isEmpty()) null else pageKey + 1
            )
        } catch (exception: Exception) {
            coroutineContext.ensureActive()
            LoadResult.Error(exception)
        }
    }
}