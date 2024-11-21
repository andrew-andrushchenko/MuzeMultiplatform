package com.andrii_a.muze.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.andrii_a.muze.data.local.MuzeDatabase
import org.koin.dsl.module
import java.io.File

actual val platformDatabaseModule = module {
    single<RoomDatabase.Builder<MuzeDatabase>> {
        val dbFile = File(System.getProperty("java.io.tmpdir"), "my_room.db")

        Room.databaseBuilder<MuzeDatabase>(
            name = dbFile.absolutePath,
        )
    }
}