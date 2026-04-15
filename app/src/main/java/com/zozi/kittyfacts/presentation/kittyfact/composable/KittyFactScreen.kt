package com.zozi.kittyfacts.presentation.kittyfact.composable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.presentation.kittyfact.composable.model.DiscoverUiModel
import com.zozi.kittyfacts.presentation.kittyfact.composable.model.KittyFactUiModel
import com.zozi.kittyfacts.presentation.kittyfact.viewmodel.KittyFactViewModel
import com.zozi.kittyfacts.presentation.theme.KittyFactsTheme

@Composable
fun KittyFactScreen(
    modifier: Modifier = Modifier,
    viewModel: KittyFactViewModel = hiltViewModel()
) {
    val uiModel by viewModel.kittyFactUiModel.collectAsStateWithLifecycle()

    KittyFactScreenContent(
        modifier = modifier,
        uiModel = uiModel,
    )
}

@Composable
private fun KittyFactScreenContent(
    modifier: Modifier = Modifier,
    uiModel: KittyFactUiModel,
) {
    KittyFactsHome(
        modifier = modifier,
        favoritesCount = uiModel.favorites.size,
        discover = {
            DiscoverTab(
                model = uiModel.discover,
            )
        },
        favorites = {
            FavoritesTab(
                favorites = uiModel.favorites,
                query = uiModel.favoritesQuery,
                onQueryChange = uiModel.onFavoritesQueryChange,
                onRemoveFavorite = uiModel.onRemoveFavorite,
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun KittyFactScreenPreview() {
    KittyFactsTheme {
        KittyFactScreenContent(
            uiModel = KittyFactUiModel(
                discover = DiscoverUiModel(
                    fact = "Cats sleep 12–16 hours a day.",
                    isLoading = false,
                    errorResId = null,
                    isFavorite = false,
                    onNewFact = {},
                    onToggleFavorite = {},
                ),
                favorites = listOf(
                    KittyFact(id = 1, text = "Cats purr to communicate."),
                    KittyFact(id = 2, text = "A group of cats is called a clowder."),
                ),
                favoritesQuery = "",
                onFavoritesQueryChange = {},
                onRemoveFavorite = {},
            ),
        )
    }
}
