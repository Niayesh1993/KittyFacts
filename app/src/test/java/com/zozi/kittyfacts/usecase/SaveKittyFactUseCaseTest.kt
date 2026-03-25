package com.zozi.kittyfacts.usecase

import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import com.zozi.kittyfacts.domain.usecase.SaveKittyFactUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SaveKittyFactUseCaseTest {

    private val repository: KittyFactRepository = mockk(relaxed = true)
    private lateinit var useCase: SaveKittyFactUseCase

    @Before
    fun setup() {
        useCase = SaveKittyFactUseCase(repository)
    }

    @Test
    fun `invoke should call repository saveFact`() = runTest {
        // given
        val fact = KittyFact(text = "Cats love naps")

        // when
        useCase(fact)

        // then
        coVerify(exactly = 1) {
            repository.saveFact(fact)
        }
    }

    @Test
    fun `invoke propagates exception when repository save fails`() = runTest {
        // given
        val fact = KittyFact(text = "Cats love naps")
        coEvery { repository.saveFact(fact) } throws IllegalStateException("db")

        // when/then
        assertThrows(IllegalStateException::class.java) {
            runBlocking { useCase(fact) }
        }
    }
}