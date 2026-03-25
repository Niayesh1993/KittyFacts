package com.zozi.kittyfacts.domain.usecase

import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import javax.inject.Inject

class GetRandomKittyFactUseCase @Inject constructor(
    private val repository: KittyFactRepository
) {

    suspend operator fun invoke(): Result<KittyFact> {
        return repository.getRandomFact()
    }
}