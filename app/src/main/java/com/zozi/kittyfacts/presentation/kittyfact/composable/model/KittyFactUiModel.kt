package com.zozi.kittyfacts.presentation.kittyfact.composable.model

import com.zozi.kittyfacts.domain.model.KittyFact

data class KittyFactUiModel(
    val discover: DiscoverUiModel,
    val favorites: List<KittyFact>,
    val favoritesQuery: String,
    val onFavoritesQueryChange: (String) -> Unit,
    val onRemoveFavorite: (Long) -> Unit,
)
