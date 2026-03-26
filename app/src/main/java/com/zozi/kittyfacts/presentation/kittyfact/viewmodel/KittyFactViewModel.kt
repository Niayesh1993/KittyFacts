package com.zozi.kittyfacts.presentation.kittyfact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.usecase.GetRandomKittyFactUseCase
import com.zozi.kittyfacts.domain.usecase.GetSavedKittyFactsUseCase
import com.zozi.kittyfacts.domain.usecase.RemoveKittyFactUseCase
import com.zozi.kittyfacts.domain.usecase.SaveKittyFactUseCase
import com.zozi.kittyfacts.presentation.kittyfact.error.KittyFactErrorMapper
import com.zozi.kittyfacts.presentation.kittyfact.state.KittyFactUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KittyFactViewModel @Inject constructor(
    private val getRandomKittyFact: GetRandomKittyFactUseCase,
    private val saveKittyFact: SaveKittyFactUseCase,
    private val getSavedFacts: GetSavedKittyFactsUseCase,
    private val removeFact: RemoveKittyFactUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(KittyFactUiState())
    val uiState: StateFlow<KittyFactUiState> = _uiState

    val favorites = getSavedFacts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    /**
     * Ensures that we trigger the initial network request at most once per ViewModel lifecycle.
     *
     * The screen may be recreated (e.g., rotation) and re-run effects; this method prevents
     * duplicate API calls in those cases.
     */
    private var didLoadInitialFact: Boolean = false

    fun ensureInitialFactLoaded() {
        if (didLoadInitialFact) return
        didLoadInitialFact = true

        // Only fetch if we don't already have something to show.
        val ui = _uiState.value
        if (ui.fact.isBlank() && ui.errorResId == null && !ui.isLoading) {
            fetchFact()
        }
    }

    fun fetchFact() {
        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true, errorResId = null) }

            val result = getRandomKittyFact()

            result.onSuccess { fact ->
                _uiState.update {
                    it.copy(
                        fact = fact.text,
                        isLoading = false
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorResId = KittyFactErrorMapper.toMessageResId(throwable)
                    )
                }
            }
        }
    }

    fun saveCurrentFact() {
        val ui = _uiState.value
        val factText = ui.fact

        // Don't allow saving while loading or when the current screen is showing an error.
        if (ui.isLoading || ui.errorResId != null) return

        if (factText.isNotEmpty()) {
            viewModelScope.launch {
                saveKittyFact(KittyFact(text = factText))
            }
        }
    }

    fun toggleCurrentFavorite() {
        val ui = _uiState.value
        val factText = ui.fact

        // Don't allow toggling while loading or when the current screen is showing an error.
        if (ui.isLoading || ui.errorResId != null) return

        if (factText.isBlank()) return

        viewModelScope.launch {
            val isAlreadyFavorite = favorites.value.any { it.text == factText }
            if (isAlreadyFavorite) {
                removeFact.byText(factText)
            } else {
                saveKittyFact(KittyFact(text = factText))
            }
        }
    }

    fun removeFavoriteById(id: Long) {
        viewModelScope.launch {
            removeFact.byId(id)
        }
    }
}