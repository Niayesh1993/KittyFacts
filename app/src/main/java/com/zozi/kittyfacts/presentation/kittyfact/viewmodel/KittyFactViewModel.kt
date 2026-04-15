package com.zozi.kittyfacts.presentation.kittyfact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.usecase.GetRandomKittyFactUseCase
import com.zozi.kittyfacts.domain.usecase.GetSavedKittyFactsUseCase
import com.zozi.kittyfacts.domain.usecase.RemoveKittyFactUseCase
import com.zozi.kittyfacts.domain.usecase.SaveKittyFactUseCase
import com.zozi.kittyfacts.domain.usecase.SearchSavedKittyFactsUseCase
import com.zozi.kittyfacts.presentation.kittyfact.composable.factory.KittyFactUiModelFactory
import com.zozi.kittyfacts.presentation.kittyfact.composable.model.DiscoverUiModel
import com.zozi.kittyfacts.presentation.kittyfact.composable.model.KittyFactUiModel
import com.zozi.kittyfacts.presentation.kittyfact.error.KittyFactErrorMapper
import com.zozi.kittyfacts.presentation.kittyfact.state.KittyFactUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KittyFactViewModel @Inject constructor(
    private val getRandomKittyFact: GetRandomKittyFactUseCase,
    private val saveKittyFact: SaveKittyFactUseCase,
    private val getSavedFacts: GetSavedKittyFactsUseCase,
    private val searchSavedKittyFacts: SearchSavedKittyFactsUseCase,
    private val removeFact: RemoveKittyFactUseCase,
    private val kittyFactUiModelFactory: KittyFactUiModelFactory,
) : ViewModel() {

    private val uiState = MutableStateFlow(KittyFactUiState())

    private val favoritesQuery = MutableStateFlow("")

    private val favorites = favoritesQuery
        .debounce(300L)
        .distinctUntilChanged()
        .flatMapLatest { query ->
            if (query.isBlank()) {
                getSavedFacts()
            } else {
                searchSavedKittyFacts(query)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val kittyFactUiModel: StateFlow<KittyFactUiModel> =
        combine(uiState, favorites, favoritesQuery) { state, favoritesList, query ->
            val isFavorite = state.fact.isNotBlank() && favoritesList.any { it.text == state.fact }

            kittyFactUiModelFactory.make(
                discover = DiscoverUiModel(
                    fact = state.fact,
                    isLoading = state.isLoading,
                    errorResId = state.errorResId,
                    isFavorite = isFavorite,
                    onNewFact = ::fetchFact,
                    onToggleFavorite = ::toggleCurrentFavorite,
                ),
                favorites = favoritesList,
                favoritesQuery = query,
                onFavoritesQueryChange = ::onFavoritesQueryChange,
                onRemoveFavorite = ::removeFavoriteById,
            )
        }
            .onStart{ fetchFact() }
            .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = kittyFactUiModelFactory.make(
                discover = DiscoverUiModel(
                    fact = "",
                    isLoading = false,
                    errorResId = null,
                    isFavorite = false,
                    onNewFact = ::fetchFact,
                    onToggleFavorite = ::toggleCurrentFavorite,
                ),
                favorites = emptyList(),
                favoritesQuery = "",
                onFavoritesQueryChange = ::onFavoritesQueryChange,
                onRemoveFavorite = ::removeFavoriteById,
            )
        )


    private fun onFavoritesQueryChange(query: String) {
        favoritesQuery.value = query
    }

    private fun fetchFact() {
        viewModelScope.launch {

            uiState.update { it.copy(isLoading = true, errorResId = null) }

            val result = getRandomKittyFact()

            result.onSuccess { fact ->
                uiState.update {
                    it.copy(
                        fact = fact.text,
                        isLoading = false
                    )
                }
            }.onFailure { throwable ->
                uiState.update {
                    it.copy(
                        isLoading = false,
                        errorResId = KittyFactErrorMapper.toMessageResId(throwable)
                    )
                }
            }
        }
    }

    private fun toggleCurrentFavorite() {
        val ui = uiState.value
        val factText = ui.fact

        // Don't allow toggling while loading or when the current screen is showing an error.
        if (ui.isLoading || ui.errorResId != null) return

        if (factText.isBlank()) return

        viewModelScope.launch {
            val existingFavorite = favorites.value.firstOrNull { it.text == factText }
            if (existingFavorite != null) {
                // Remove by id to keep the data layer API simple and avoid text-based deletes.
                removeFact.byId(existingFavorite.id)
            } else {
                saveKittyFact(KittyFact(text = factText))
            }
        }
    }

    private fun removeFavoriteById(id: Long) {
        viewModelScope.launch {
            removeFact.byId(id)
        }
    }
}