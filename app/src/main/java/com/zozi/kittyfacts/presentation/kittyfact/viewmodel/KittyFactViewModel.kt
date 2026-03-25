package com.zozi.kittyfacts.presentation.kittyfact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.domain.usecase.GetRandomKittyFactUseCase
import com.zozi.kittyfacts.domain.usecase.GetSavedKittyFactsUseCase
import com.zozi.kittyfacts.domain.usecase.SaveKittyFactUseCase
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
    private val saveFact: SaveKittyFactUseCase,
    private val getSavedFacts: GetSavedKittyFactsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(KittyFactUiState())
    val uiState: StateFlow<KittyFactUiState> = _uiState

    val favorites = getSavedFacts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    fun fetchFact() {
        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true, error = null) }

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
                        error = throwable.message ?: "Failed to load fact"
                    )
                }
            }
        }
    }

    fun saveCurrentFact() {
        val factText = _uiState.value.fact
        if (factText.isNotEmpty()) {
            viewModelScope.launch {
                saveFact(KittyFact(text = factText))
            }
        }
    }
}