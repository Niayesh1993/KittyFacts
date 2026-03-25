package com.zozi.kittyfacts.presentation.kittyfact.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.zozi.kittyfacts.R
import com.zozi.kittyfacts.domain.model.KittyFact

@Composable
fun FavoritesTab(
    favorites: List<KittyFact>,
    onRemoveFavorite: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.favorites_title),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "${pluralStringResource(R.plurals.favorites_saved_facts, favorites.size, favorites.size)} • ${stringResource(R.string.available_offline)}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (favorites.isEmpty()) {
            FavoritesEmptyState()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favorites) { fact ->
                    FavoritesItemCard(
                        fact = fact,
                        onRemove = onRemoveFavorite,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
