package com.zozi.kittyfacts.domain.usecase

import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import javax.inject.Inject

class SaveKittyFactUseCase @Inject constructor(
    private val repository: KittyFactRepository
) {
    suspend operator fun invoke(fact: KittyFact) {
        repository.saveFact(fact)
    }
}