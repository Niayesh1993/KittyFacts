package com.zozi.kittyfacts.usecase

import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import com.zozi.kittyfacts.domain.usecase.GetRandomKittyFactUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetRandomKittyFactUseCaseTest {

    private val repository: KittyFactRepository = mockk()
    private lateinit var useCase: GetRandomKittyFactUseCase

    @Before
    fun setup() {
        useCase = GetRandomKittyFactUseCase(repository)
    }

    @Test
    fun `invoke returns data from repository`() = runTest {
        // given
        val fact = KittyFact("Cats are amazing")
        coEvery { repository.getRandomFact() } returns Result.success(fact)

        // when
        val result = useCase()

        // then
        assertTrue(result.isSuccess)
        assertEquals("Cats are amazing", result.getOrNull()?.text)
    }
}