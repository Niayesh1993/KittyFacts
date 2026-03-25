package com.zozi.kittyfacts.usecase

import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import com.zozi.kittyfacts.domain.usecase.GetSavedKittyFactsUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GetSavedKittyFactsUseCaseTest {

    private val repository: KittyFactRepository = mockk()
    private lateinit var useCase: GetSavedKittyFactsUseCase

    @Before
    fun setup() {
        useCase = GetSavedKittyFactsUseCase(repository)
    }

    @Test
    fun `invoke should return flow of saved facts`() = runTest {
        // given
        val facts = listOf(
            KittyFact(text = "Fact 1"),
            KittyFact(text = "Fact 2")
        )

        every { repository.getSavedFacts() } returns flowOf(facts)

        // when
        val emitted = useCase().first()

        // then
        assertEquals(2, emitted.size)
        assertEquals("Fact 1", emitted[0].text)
        verify(exactly = 1) { repository.getSavedFacts() }
    }

    @Test
    fun `invoke should emit empty list when no saved facts`() = runTest {
        // given
        every { repository.getSavedFacts() } returns flowOf(emptyList())

        // when
        val emitted = useCase().first()

        // then
        assertEquals(0, emitted.size)
        verify(exactly = 1) { repository.getSavedFacts() }
    }
}