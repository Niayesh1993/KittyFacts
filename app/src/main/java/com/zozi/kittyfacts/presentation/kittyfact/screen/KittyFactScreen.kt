package com.zozi.kittyfacts.presentation.kittyfact.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zozi.kittyfacts.domain.model.KittyFact
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Title
        Text(
            text = "Kitty Facts 🐱",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Fact Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Text(
                        text = state.error!!,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {
                    Text(
                        text = state.fact,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            Button(
                onClick = { viewModel.fetchFact() },
                modifier = Modifier.weight(1f)
            ) {
                Text("New Fact")
            }

            Button(
                onClick = { viewModel.saveCurrentFact() },
                modifier = Modifier.weight(1f),
                enabled = state.fact.isNotEmpty()
            ) {
                Text("Save ❤️")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        // Favorites Title
        Text(
            text = "Favorites",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Favorites List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(favorites) { fact ->
                Card {
                    Text(
                        text = fact.text,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun KittyFactScreen(
    modifier: Modifier = Modifier,
    state: KittyFactUiState,
    favorites: List<KittyFact>,
    onNewFact: () -> Unit,
    onSave: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Title
        Text(
            text = "Kitty Facts 🐱",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Fact Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Text(
                        text = state.error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }

                else -> {
                    Text(
                        text = state.fact,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Actions
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

            Button(
                onClick = onNewFact,
                modifier = Modifier.weight(1f)
            ) {
                Text("New Fact")
            }

            Button(
                onClick = onSave,
                modifier = Modifier.weight(1f),
                enabled = state.fact.isNotEmpty()
            ) {
                Text("Save ❤️")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        HorizontalDivider()

        Spacer(modifier = Modifier.height(16.dp))

        // Favorites Title
        Text(
            text = "Favorites",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Favorites List
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(favorites) { fact ->
                Card {
                    Text(
                        text = fact.text,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KittyFactScreenPreview() {
    KittyFactsTheme {
        KittyFactScreen(
            state = KittyFactUiState(fact = "Cats sleep 12–16 hours a day."),
            favorites = listOf(
                KittyFact(id = 1, text = "Cats purr to communicate."),
                KittyFact(id = 2, text = "A group of cats is called a clowder."),
            ),
            onNewFact = {},
            onSave = {},
        )
    }
}
