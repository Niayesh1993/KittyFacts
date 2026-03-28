package com.zozi.kittyfacts.usecase

import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import com.zozi.kittyfacts.domain.usecase.SearchSavedKittyFactsUseCase
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
class SearchSavedKittyFactsUseCaseTest {

    private val repository: KittyFactRepository = mockk()
    private lateinit var useCase: SearchSavedKittyFactsUseCase

    @Before
    fun setup() {
        useCase = SearchSavedKittyFactsUseCase(repository)
    }

    @Test
    fun `invoke should return flow of searched facts`() = runTest {
        // given
        val query = "purr"
        val facts = listOf(
            KittyFact(text = "Cats purr"),
            KittyFact(text = "Some cats purr loudly"),
        )

        every { repository.searchSavedFacts(query) } returns flowOf(facts)

        // when
        val emitted = useCase(query).first()

        // then
        assertEquals(2, emitted.size)
        assertEquals("Cats purr", emitted[0].text)
        verify(exactly = 1) { repository.searchSavedFacts(query) }
    }

    @Test
    fun `invoke should emit empty list when no match`() = runTest {
        // given
        val query = "nope"
        every { repository.searchSavedFacts(query) } returns flowOf(emptyList())

        // when
        val emitted = useCase(query).first()

        // then
        assertEquals(0, emitted.size)
        verify(exactly = 1) { repository.searchSavedFacts(query) }
    }
}

