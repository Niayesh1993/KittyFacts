package com.zozi.kittyfacts.viewmodel

import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import com.zozi.kittyfacts.domain.usecase.GetRandomKittyFactUseCase
import com.zozi.kittyfacts.presentation.kittyfact.viewmodel.KittyFactViewModel
import com.zozi.kittyfacts.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class KittyFactViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private val repository: KittyFactRepository = mockk()
    private val useCase = GetRandomKittyFactUseCase(repository)

    private lateinit var viewModel: KittyFactViewModel

    @Before
    fun setup() {
        viewModel = KittyFactViewModel(useCase)
    }

    @Test
    fun `fetchFact success updates state`() = runTest {
        // given
        val fact = KittyFact("Cats sleep 16 hours a day")
        coEvery { repository.getRandomFact() } returns Result.success(fact)

        // when
        viewModel.fetchFact()

        // then
        val state = viewModel.uiState.value
        assertEquals("Cats sleep 16 hours a day", state.fact)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `fetchFact error updates state`() = runTest {
        // given
        coEvery { repository.getRandomFact() } returns Result.failure(Exception())

        // when
        viewModel.fetchFact()

        // then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.error)
    }

    @Test
    fun `fetchFact shows loading first`() = runTest {
        // given
        coEvery { repository.getRandomFact() } coAnswers {
            delay(100)
            Result.success(KittyFact("Test"))
        }

        // when
        viewModel.fetchFact()

        // then
        assertTrue(viewModel.uiState.value.isLoading)
    }
}