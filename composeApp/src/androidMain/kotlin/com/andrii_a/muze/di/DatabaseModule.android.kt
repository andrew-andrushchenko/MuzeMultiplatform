package com.andrii_a.muze.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.andrii_a.muze.data.local.MuzeDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val platformDatabaseModule = module {
    single<RoomDatabase.Builder<MuzeDatabase>> {
        val appContext = androidContext().applicationContext
        val dbFile = appContext.getDatabasePath("my_room.db")

        Room.databaseBuilder<MuzeDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}