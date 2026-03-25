package com.zozi.kittyfacts.domain.usecase

import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSavedKittyFactsUseCase @Inject constructor(
    private val repository: KittyFactRepository
) {
    operator fun invoke(): Flow<List<KittyFact>> {
        return repository.getSavedFacts()
    }
}