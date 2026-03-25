package com.zozi.kittyfacts.domain.usecase

import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import javax.inject.Inject

class RemoveKittyFactUseCase @Inject constructor(
    private val repository: KittyFactRepository
) {
    suspend fun byId(id: Long) {
        repository.removeFactById(id)
    }

    suspend fun byText(text: String) {
        repository.removeFactByText(text)
    }
}

