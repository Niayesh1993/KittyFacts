package com.zozi.kittyfacts.presentation.kittyfact.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zozi.kittyfacts.domain.usecase.GetRandomKittyFactUseCase
import com.zozi.kittyfacts.presentation.kittyfact.state.KittyFactUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KittyFactViewModel @Inject constructor(
    private val getRandomKittyFact: GetRandomKittyFactUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(KittyFactUiState())
    val uiState: StateFlow<KittyFactUiState> = _uiState

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
}