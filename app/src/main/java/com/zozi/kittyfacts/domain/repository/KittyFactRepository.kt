package com.zozi.kittyfacts.domain.repository

import com.zozi.kittyfacts.domain.model.KittyFact
import kotlinx.coroutines.flow.Flow

interface KittyFactRepository {

    suspend fun getRandomFact(): Result<KittyFact>

    suspend fun saveFact(fact: KittyFact)

    suspend fun removeFactById(id: Long)

    fun getSavedFacts(): Flow<List<KittyFact>>
}