package com.zozi.kittyfacts.di

import android.content.Context
import androidx.room.Room
import com.zozi.kittyfacts.data.local.dao.KittyFactDao
import com.zozi.kittyfacts.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "kitty_db"
        ).build()
    }

    @Provides
    fun provideDao(db: AppDatabase): KittyFactDao {
        return db.kittyFactDao()
    }
}