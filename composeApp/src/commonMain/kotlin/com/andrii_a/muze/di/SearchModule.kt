package com.andrii_a.muze.di

import com.andrii_a.muze.data.local.repository.RecentSearchesRepositoryImpl
import com.andrii_a.muze.data.remote.repository.SearchRepositoryImpl
import com.andrii_a.muze.data.remote.service.SearchService
import com.andrii_a.muze.data.remote.service.SearchServiceImpl
import com.andrii_a.muze.domain.repository.RecentSearchesRepository
import com.andrii_a.muze.domain.repository.SearchRepository
import com.andrii_a.muze.ui.search.SearchViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val searchModule = module {
    singleOf(::SearchServiceImpl) { bind<SearchService>() }

    singleOf(::SearchRepositoryImpl) { bind<SearchRepository>() }
    singleOf(::RecentSearchesRepositoryImpl) { bind<RecentSearchesRepository>() }
    viewModelOf(::SearchViewModel)
}