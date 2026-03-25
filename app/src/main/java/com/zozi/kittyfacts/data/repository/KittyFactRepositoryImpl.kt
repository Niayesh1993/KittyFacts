package com.zozi.kittyfacts.data.repository

import com.zozi.kittyfacts.data.mapper.toDomain
import com.zozi.kittyfacts.data.remote.api.KittyFactApi
import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import javax.inject.Inject

class KittyFactRepositoryImpl @Inject constructor(
    private val api: KittyFactApi
) : KittyFactRepository {

    override suspend fun getRandomFact(): Result<KittyFact> {
        return try {
            val response = api.getRandomFact()
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}