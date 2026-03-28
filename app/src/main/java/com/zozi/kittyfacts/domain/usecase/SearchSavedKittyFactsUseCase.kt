package com.zozi.kittyfacts.domain.usecase

import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchSavedKittyFactsUseCase @Inject constructor(
    private val repository: KittyFactRepository
) {
    operator fun invoke(query: String): Flow<List<KittyFact>> {
        return repository.searchSavedFacts(query)
    }
}

