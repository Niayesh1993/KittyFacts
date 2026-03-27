package com.zozi.kittyfacts.data.repository

import com.zozi.kittyfacts.data.local.dao.KittyFactDao
import com.zozi.kittyfacts.data.mapper.toDomain
import com.zozi.kittyfacts.data.mapper.toEntity
import com.zozi.kittyfacts.data.remote.api.KittyFactApi
import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class KittyFactRepositoryImpl @Inject constructor(
    private val api: KittyFactApi,
    private val dao: KittyFactDao
) : KittyFactRepository {

    override suspend fun getRandomFact(): Result<KittyFact> {
        return try {
            val response = api.getRandomFact()
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveFact(fact: KittyFact) {
        dao.insertFact(fact.toEntity())
    }

    override fun getSavedFacts(): Flow<List<KittyFact>> {
        return dao.getAllFacts().map { list ->
            list.map { it.toDomain() }
        }
    }

    override suspend fun removeFactById(id: Long) {
        dao.deleteFactById(id)
    }
}