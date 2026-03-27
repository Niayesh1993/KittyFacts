package com.zozi.kittyfacts.presentation.kittyfact.composable.model

import androidx.annotation.StringRes

/**
 * UI model for the Discover tab only.
 */
data class DiscoverUiModel(
    val fact: String,
    val isLoading: Boolean,
    @StringRes val errorResId: Int?,
    val isFavorite: Boolean,
    val onNewFact: () -> Unit,
    val onToggleFavorite: () -> Unit,
)
