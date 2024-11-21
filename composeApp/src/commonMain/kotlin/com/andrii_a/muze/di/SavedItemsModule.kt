package com.andrii_a.muze.di

import com.andrii_a.muze.data.local.repository.SavedItemsRepositoryImpl
import com.andrii_a.muze.domain.repository.SavedItemsRepository
import com.andrii_a.muze.ui.saved_items.SavedItemsViewModel
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val savedItemsModule = module {
    singleOf(::SavedItemsRepositoryImpl) { bind<SavedItemsRepository>() }

    viewModelOf(::SavedItemsViewModel)
}