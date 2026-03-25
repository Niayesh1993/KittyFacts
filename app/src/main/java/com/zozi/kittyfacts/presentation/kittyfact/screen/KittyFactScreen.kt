package com.zozi.kittyfacts.presentation.kittyfact.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.zozi.kittyfacts.domain.model.KittyFact
import com.zozi.kittyfacts.presentation.kittyfact.component.DiscoverTab
import com.zozi.kittyfacts.presentation.kittyfact.component.FavoritesTab
import com.zozi.kittyfacts.presentation.kittyfact.component.KittyFactsHome
import com.zozi.kittyfacts.presentation.kittyfact.state.KittyFactUiState
import com.zozi.kittyfacts.presentation.kittyfact.viewmodel.KittyFactViewModel
import com.zozi.kittyfacts.presentation.theme.KittyFactsTheme

@Composable
fun KittyFactScreen(
    modifier: Modifier = Modifier,
    viewModel: KittyFactViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    val favorites by viewModel.favorites.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchFact()
    }

    KittyFactScreenContent(
        modifier = modifier,
        state = state,
        favorites = favorites,
        onNewFact = { viewModel.fetchFact() },
        onToggleFavorite = { viewModel.toggleCurrentFavorite() },
        onRemoveFavorite = { viewModel.removeFavoriteById(it) }
    )
}

@Composable
private fun KittyFactScreenContent(
    modifier: Modifier = Modifier,
    state: KittyFactUiState,
    favorites: List<KittyFact>,
    onNewFact: () -> Unit,
    onToggleFavorite: () -> Unit,
    onRemoveFavorite: (Long) -> Unit,
) {
    val isFavorited = state.fact.isNotBlank() && favorites.any { it.text == state.fact }

    KittyFactsHome(
        modifier = modifier,
        favoritesCount = favorites.size,
        discover = {
            DiscoverTab(
                state = state,
                onNewFact = onNewFact,
                onToggleFavorite = onToggleFavorite,
                isFavorited = isFavorited,
            )
        },
        favorites = {
            FavoritesTab(
                favorites = favorites,
                onRemoveFavorite = onRemoveFavorite,
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun KittyFactScreenPreview() {
    KittyFactsTheme {
        KittyFactScreenContent(
            state = KittyFactUiState(fact = "Cats sleep 12–16 hours a day."),
            favorites = listOf(
                KittyFact(id = 1, text = "Cats purr to communicate."),
                KittyFact(id = 2, text = "A group of cats is called a clowder."),
            ),
            onNewFact = {},
            onToggleFavorite = {},
            onRemoveFavorite = {},
        )
    }
}
