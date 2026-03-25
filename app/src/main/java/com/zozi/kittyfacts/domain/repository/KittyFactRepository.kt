package com.zozi.kittyfacts.domain.repository

import com.zozi.kittyfacts.domain.model.KittyFact
import kotlinx.coroutines.flow.Flow

interface KittyFactRepository {

    suspend fun getRandomFact(): Result<KittyFact>

    suspend fun saveFact(fact: KittyFact)

    fun getSavedFacts(): Flow<List<KittyFact>>
}