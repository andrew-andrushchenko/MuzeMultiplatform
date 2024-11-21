package com.andrii_a.muze.di

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import org.koin.dsl.module

actual val baseNetworkModule = module {
    single<HttpClient> { createHttpClient(OkHttp.create()) }
}