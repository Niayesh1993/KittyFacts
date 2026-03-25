package com.zozi.kittyfacts.di

import com.zozi.kittyfacts.data.repository.KittyFactRepositoryImpl
import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindRepository(
        impl: KittyFactRepositoryImpl
    ): KittyFactRepository
}