package com.zozi.kittyfacts.domain.repository

import com.zozi.kittyfacts.domain.model.KittyFact

interface KittyFactRepository {

    suspend fun getRandomFact(): Result<KittyFact>
}