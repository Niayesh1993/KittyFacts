package com.zozi.kittyfacts.viewmodel

import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import com.zozi.kittyfacts.domain.usecase.GetRandomKittyFactUseCase
import com.zozi.kittyfacts.domain.usecase.GetSavedKittyFactsUseCase
import com.zozi.kittyfacts.domain.usecase.RemoveKittyFactUseCase
import com.zozi.kittyfacts.domain.usecase.SaveKittyFactUseCase
import com.zozi.kittyfacts.presentation.kittyfact.viewmodel.KittyFactViewModel
import com.zozi.kittyfacts.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
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

    private val repository: KittyFactRepository = mockk(relaxed = true)
    private val getRandomKittyFactUseCase = GetRandomKittyFactUseCase(repository)
    private val saveKittyFactUseCase = SaveKittyFactUseCase(repository)
    private val getSavedKittyFactsUseCase = GetSavedKittyFactsUseCase(repository)
    private val removeKittyFactUseCase = RemoveKittyFactUseCase(repository)

    private lateinit var viewModel: KittyFactViewModel

    @Before
    fun setup() {
        // ViewModel initializes favorites flow immediately; stub it for all tests.
        coEvery { repository.getSavedFacts() } returns flowOf(emptyList())

        viewModel = KittyFactViewModel(
            getRandomKittyFactUseCase,
            saveKittyFactUseCase,
            getSavedKittyFactsUseCase,
            removeKittyFactUseCase,
        )
    }

    @Test
    fun `fetchFact success updates state`() = runTest {
        // given
        val fact = KittyFact(12, "Cats sleep 16 hours a day")
        coEvery { repository.getRandomFact() } returns Result.success(fact)

        // when
        viewModel.fetchFact()
        advanceUntilIdle()

        // then
        val state = viewModel.uiState.value
        assertEquals("Cats sleep 16 hours a day", state.fact)
        assertFalse(state.isLoading)
        assertNull(state.errorResId)
    }

    @Test
    fun `fetchFact error updates state`() = runTest {
        // given
        coEvery { repository.getRandomFact() } returns Result.failure(Exception("Boom"))

        // when
        viewModel.fetchFact()
        advanceUntilIdle()

        // then
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNotNull(state.errorResId)
    }

    @Test
    fun `fetchFact shows loading first`() = runTest {
        // given
        coEvery { repository.getRandomFact() } coAnswers {
            delay(100)
            Result.success(KittyFact(1, "Test"))
        }

        // when
        viewModel.fetchFact()
        runCurrent()

        // then
        assertTrue(viewModel.uiState.value.isLoading)
    }

    @Test
    fun `toggleCurrentFavorite saves when current fact isn't favorited`() = runTest {
        // given
        val factText = "Cats have whiskers"
        coEvery { repository.getSavedFacts() } returns flowOf(emptyList())
        coEvery { repository.getRandomFact() } returns Result.success(KittyFact(1, factText))

        // recreate VM so favorites stateIn uses the stubbed flow
        viewModel = KittyFactViewModel(
            getRandomKittyFactUseCase,
            saveKittyFactUseCase,
            getSavedKittyFactsUseCase,
            removeKittyFactUseCase,
        )

        viewModel.fetchFact()
        advanceUntilIdle()

        // when
        viewModel.toggleCurrentFavorite()
        advanceUntilIdle()

        // then
        coVerify(exactly = 1) { repository.saveFact(match { it.text == factText }) }
        coVerify(exactly = 0) { repository.removeFactByText(any()) }
    }

    @Test
    fun `removeFavoriteById calls repository removeFactById`() = runTest {
        // when
        viewModel.removeFavoriteById(7L)
        advanceUntilIdle()

        // then
        coVerify(exactly = 1) { repository.removeFactById(7L) }
    }
}