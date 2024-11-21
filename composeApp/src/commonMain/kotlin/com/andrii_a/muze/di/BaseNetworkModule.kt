package com.andrii_a.muze.di

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.Module

expect val baseNetworkModule: Module

fun createHttpClient(engine: HttpClientEngine): HttpClient {
    return HttpClient(engine) {
            install(Logging) {
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(HttpTimeout) {
                connectTimeoutMillis = 30000
                requestTimeoutMillis = 30000
            }
        }
}