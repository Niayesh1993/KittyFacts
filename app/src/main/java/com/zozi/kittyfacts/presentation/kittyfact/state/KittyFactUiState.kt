package com.zozi.kittyfacts.presentation.kittyfact.state

data class KittyFactUiState(
    val fact: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)
