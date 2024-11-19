package com.andrii_a.muze.data.local

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.andrii_a.muze.data.local.dao.RecentSearchesDao
import com.andrii_a.muze.data.local.dao.SavedItemsDao
import com.andrii_a.muze.data.local.entity.RecentSearchItemEntity
import com.andrii_a.muze.data.local.entity.SavedItemEntity

@Database(
    entities = [RecentSearchItemEntity::class, SavedItemEntity::class],
    version = 1
)
@ConstructedBy(MuzeDatabaseConstructor::class)
abstract class MuzeDatabase : RoomDatabase() {

    abstract fun recentSearchesDao(): RecentSearchesDao

    abstract fun savedItemsDao(): SavedItemsDao
}

@Suppress("NO_ACTUAL_FOR_EXPECT", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object MuzeDatabaseConstructor : RoomDatabaseConstructor<MuzeDatabase> {
    override fun initialize(): MuzeDatabase
}