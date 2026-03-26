package com.zozi.kittyfacts.presentation.kittyfact.state

import androidx.annotation.StringRes

data class KittyFactUiState(
    val fact: String = "",
    val isLoading: Boolean = false,
    @StringRes val errorResId: Int? = null,
)
