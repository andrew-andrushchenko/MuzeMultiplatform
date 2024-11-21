package com.andrii_a.muze.di

import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.andrii_a.muze.data.local.MuzeDatabase
import com.andrii_a.muze.data.local.dao.RecentSearchesDao
import com.andrii_a.muze.data.local.dao.SavedItemsDao
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformDatabaseModule: Module

val mainDatabaseModule = module {
    single<MuzeDatabase> { getRoomDatabase(get()) }

    factory<RecentSearchesDao> { getRecentSearchesDao(get()) }

    factory<SavedItemsDao> { getSavedItemsDao(get()) }
}

private fun getRoomDatabase(builder: RoomDatabase.Builder<MuzeDatabase>): MuzeDatabase {
    return builder
        .setDriver(BundledSQLiteDriver())
        .build()
}

private fun getRecentSearchesDao(database: MuzeDatabase): RecentSearchesDao {
    return database.recentSearchesDao()
}

private fun getSavedItemsDao(database: MuzeDatabase): SavedItemsDao {
    return database.savedItemsDao()
}
