package com.andrii_a.muze.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            baseNetworkModule,
            artistsModule,
            artworksModule,
            searchModule,
            mainDatabaseModule,
            platformDatabaseModule,
            savedItemsModule
        )
    }
}