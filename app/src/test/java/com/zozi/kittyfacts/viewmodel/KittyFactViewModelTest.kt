package com.zozi.kittyfacts.viewmodel

import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.repository.KittyFactRepository
import com.zozi.kittyfacts.domain.usecase.GetRandomKittyFactUseCase
import com.zozi.kittyfacts.domain.usecase.GetSavedKittyFactsUseCase
import com.zozi.kittyfacts.domain.usecase.RemoveKittyFactUseCase
import com.zozi.kittyfacts.domain.usecase.SaveKittyFactUseCase
import com.zozi.kittyfacts.domain.usecase.SearchSavedKittyFactsUseCase
import com.zozi.kittyfacts.presentation.kittyfact.composable.factory.KittyFactUiModelFactory
import com.zozi.kittyfacts.presentation.kittyfact.viewmodel.KittyFactViewModel
import com.zozi.kittyfacts.util.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class KittyFactViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(StandardTestDispatcher())

    private val repository: KittyFactRepository = mockk(relaxed = true)
    private val getRandomKittyFactUseCase = GetRandomKittyFactUseCase(repository)
    private val saveKittyFactUseCase = SaveKittyFactUseCase(repository)
    private val getSavedKittyFactsUseCase = GetSavedKittyFactsUseCase(repository)
    private val searchSavedKittyFactsUseCase = SearchSavedKittyFactsUseCase(repository)
    private val removeKittyFactUseCase = RemoveKittyFactUseCase(repository)
    private val uiModelFactory = KittyFactUiModelFactory()

    private lateinit var viewModel: KittyFactViewModel

    @Before
    fun setup() {
        coEvery { repository.getSavedFacts() } returns flowOf(emptyList())
        coEvery { repository.searchSavedFacts(any()) } returns flowOf(emptyList())
        coEvery { repository.getRandomFact() } returns Result.success(KittyFact(1, "Init"))
        viewModel = KittyFactViewModel(
            getRandomKittyFactUseCase,
            saveKittyFactUseCase,
            getSavedKittyFactsUseCase,
            searchSavedKittyFactsUseCase,
            removeKittyFactUseCase,
            uiModelFactory,
        )
    }

    private fun kotlinx.coroutines.test.TestScope.startUiModelCollection() {
        backgroundScope.launch { viewModel.kittyFactUiModel.collect { /* keep active */ } }
    }

    private suspend fun awaitFact(expected: String) {
        viewModel.kittyFactUiModel
            .map { it.discover.fact }
            .filter { it == expected }
            .first()
    }

    private suspend fun awaitError() {
        viewModel.kittyFactUiModel
            .map { it.discover.errorResId }
            .filter { it != null }
            .first()
    }

    @Test
    fun `onStart triggers fetch and updates discover model on success`() = runTest {
        startUiModelCollection()

        awaitFact("Init")

        val ui = viewModel.kittyFactUiModel.value
        assertEquals("Init", ui.discover.fact)
        assertFalse(ui.discover.isLoading)
        assertNull(ui.discover.errorResId)

        coVerify(exactly = 1) { repository.getRandomFact() }
    }

    @Test
    fun `init fetch failure exposes error in discover model`() = runTest {
        coEvery { repository.getRandomFact() } returns Result.failure(Exception("Boom"))

        viewModel = KittyFactViewModel(
            getRandomKittyFactUseCase,
            saveKittyFactUseCase,
            getSavedKittyFactsUseCase,
            searchSavedKittyFactsUseCase,
            removeKittyFactUseCase,
            uiModelFactory,
        )

        startUiModelCollection()
        awaitError()

        val ui = viewModel.kittyFactUiModel.value
        assertFalse(ui.discover.isLoading)
        assertNotNull(ui.discover.errorResId)
    }

    @Test
    fun `refresh via onNewFact shows loading then updates fact`() = runTest {
        startUiModelCollection()

        awaitFact("Init")

        coEvery { repository.getRandomFact() } coAnswers {
            delay(100)
            Result.success(KittyFact(2, "Next"))
        }

        viewModel.kittyFactUiModel.value.discover.onNewFact()

        // Wait until we observe the loading state.
        viewModel.kittyFactUiModel
            .map { it.discover.isLoading }
            .filter { it }
            .first()

        advanceUntilIdle()
        awaitFact("Next")

        coVerify(exactly = 2) { repository.getRandomFact() }
    }

    @Test
    fun `toggle favorite saves when current fact is not in favorites`() = runTest {
        val factText = "Cats have whiskers"
        coEvery { repository.getSavedFacts() } returns flowOf(emptyList())
        coEvery { repository.searchSavedFacts(any()) } returns flowOf(emptyList())
        coEvery { repository.getRandomFact() } returns Result.success(KittyFact(1, factText))

        viewModel = KittyFactViewModel(
            getRandomKittyFactUseCase,
            saveKittyFactUseCase,
            getSavedKittyFactsUseCase,
            searchSavedKittyFactsUseCase,
            removeKittyFactUseCase,
            uiModelFactory,
        )

        startUiModelCollection()
        awaitFact(factText)

        viewModel.kittyFactUiModel.value.discover.onToggleFavorite()
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.saveFact(match { it.text == factText }) }
        coVerify(exactly = 0) { repository.removeFactById(any()) }
    }

    @Test
    fun `toggle favorite removes by id when current fact already favorited`() = runTest {
        val factText = "Cats purr"
        val existing = KittyFact(id = 123L, text = factText)

        coEvery { repository.getSavedFacts() } returns flowOf(listOf(existing))
        coEvery { repository.searchSavedFacts(any()) } returns flowOf(emptyList())
        coEvery { repository.getRandomFact() } returns Result.success(KittyFact(1, factText))

        viewModel = KittyFactViewModel(
            getRandomKittyFactUseCase,
            saveKittyFactUseCase,
            getSavedKittyFactsUseCase,
            searchSavedKittyFactsUseCase,
            removeKittyFactUseCase,
            uiModelFactory,
        )

        startUiModelCollection()
        awaitFact(factText)

        // Wait for the debounced favorites flow to emit (favoritesQuery starts blank).
        advanceUntilIdle()

        viewModel.kittyFactUiModel.value.discover.onToggleFavorite()
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.removeFactById(123L) }
        coVerify(exactly = 0) { repository.saveFact(any()) }
    }

    @Test
    fun `toggle favorite does nothing while error is shown`() = runTest {
        coEvery { repository.getRandomFact() } returns Result.failure(Exception("offline"))

        viewModel = KittyFactViewModel(
            getRandomKittyFactUseCase,
            saveKittyFactUseCase,
            getSavedKittyFactsUseCase,
            searchSavedKittyFactsUseCase,
            removeKittyFactUseCase,
            uiModelFactory,
        )

        startUiModelCollection()
        awaitError()

        viewModel.kittyFactUiModel.value.discover.onToggleFavorite()
        advanceUntilIdle()

        coVerify(exactly = 0) { repository.saveFact(any()) }
        coVerify(exactly = 0) { repository.removeFactById(any()) }
    }

    @Test
    fun `remove favorite callback calls repository removeFactById`() = runTest {
        startUiModelCollection()

        awaitFact("Init")

        viewModel.kittyFactUiModel.value.onRemoveFavorite(7L)
        advanceUntilIdle()

        coVerify(exactly = 1) { repository.removeFactById(7L) }
    }

    @Test
    fun `favorites query triggers debounced repository search`() = runTest {
        coEvery { repository.getSavedFacts() } returns flowOf(emptyList())
        coEvery { repository.searchSavedFacts("pur") } returns flowOf(
            listOf(KittyFact(id = 1, text = "Cats purr"))
        )

        viewModel = KittyFactViewModel(
            getRandomKittyFactUseCase,
            saveKittyFactUseCase,
            getSavedKittyFactsUseCase,
            searchSavedKittyFactsUseCase,
            removeKittyFactUseCase,
            uiModelFactory,
        )

        startUiModelCollection()

        // Trigger query change via the UI model callback.
        viewModel.kittyFactUiModel.value.onFavoritesQueryChange("pur")

        advanceUntilIdle()

        // Verified via emitted favorites list.
        val favorites = viewModel.kittyFactUiModel.value.favorites
        assertEquals(1, favorites.size)
        assertEquals("Cats purr", favorites.first().text)
    }

    @Test
    fun `clearing favorites query switches back to getSavedFacts`() = runTest {
        val all = listOf(
            KittyFact(id = 1, text = "Cats purr"),
            KittyFact(id = 2, text = "Cats sleep"),
        )

        coEvery { repository.getSavedFacts() } returns flowOf(all)
        coEvery { repository.searchSavedFacts(any()) } returns flowOf(emptyList())

        viewModel = KittyFactViewModel(
            getRandomKittyFactUseCase,
            saveKittyFactUseCase,
            getSavedKittyFactsUseCase,
            searchSavedKittyFactsUseCase,
            removeKittyFactUseCase,
            uiModelFactory,
        )

        startUiModelCollection()

        viewModel.kittyFactUiModel.value.onFavoritesQueryChange("zzz")
        advanceUntilIdle()

        viewModel.kittyFactUiModel.value.onFavoritesQueryChange("")
        advanceUntilIdle()

        val favorites = viewModel.kittyFactUiModel.value.favorites
        assertEquals(2, favorites.size)
        assertEquals(all, favorites)
    }
}
