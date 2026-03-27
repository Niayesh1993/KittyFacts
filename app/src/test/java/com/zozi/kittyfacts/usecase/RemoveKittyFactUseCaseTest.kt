package com.zozi.kittyfacts.usecase

import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import com.zozi.kittyfacts.domain.usecase.RemoveKittyFactUseCase
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
class RemoveKittyFactUseCaseTest {

    private val repository: KittyFactRepository = mockk(relaxed = true)
    private lateinit var useCase: RemoveKittyFactUseCase

    @Before
    fun setup() {
        useCase = RemoveKittyFactUseCase(repository)
    }

    @Test
    fun `byId calls repository removeFactById`() = runTest {
        // when
        useCase.byId(42L)

        // then
        coVerify(exactly = 1) { repository.removeFactById(42L) }
    }

    @Test
    fun `byId propagates exception when repository delete fails`() = runTest {
        // given
        coEvery { repository.removeFactById(99L) } throws IllegalStateException("db")

        // when/then
        assertThrows(IllegalStateException::class.java) {
            runBlocking { useCase.byId(99L) }
        }
    }
}
