package com.zozi.kittyfacts.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zozi.kittyfacts.data.local.dao.KittyFactDao
import com.zozi.kittyfacts.data.local.entity.KittyFactEntity

@Database(
    entities = [KittyFactEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun kittyFactDao(): KittyFactDao
}