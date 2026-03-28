package com.zozi.kittyfacts.presentation.kittyfact.composable.factory

import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.presentation.kittyfact.composable.model.DiscoverUiModel
import com.zozi.kittyfacts.presentation.kittyfact.composable.model.KittyFactUiModel
import javax.inject.Inject

class KittyFactUiModelFactory @Inject constructor() {

    fun make(
        discover: DiscoverUiModel,
        favorites: List<KittyFact>,
        favoritesQuery: String,
        onFavoritesQueryChange: (String) -> Unit,
        onRemoveFavorite: (Long) -> Unit,
    ) = KittyFactUiModel(
        discover = discover,
        favorites = favorites,
        favoritesQuery = favoritesQuery,
        onFavoritesQueryChange = onFavoritesQueryChange,
        onRemoveFavorite = onRemoveFavorite,
    )
}