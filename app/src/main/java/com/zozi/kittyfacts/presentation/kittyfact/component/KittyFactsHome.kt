package com.zozi.kittyfacts.presentation.kittyfact.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Simple 2-tab container (Discover / Favorites) matching the provided UI direction.
 * This is intentionally lightweight and doesn't depend on Navigation.
 */
@Composable
fun KittyFactsHome(
    modifier: Modifier = Modifier,
    favoritesCount: Int = 0,
    discover: @Composable () -> Unit,
    favorites: @Composable () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                SegmentedTabItem(
                    selected = selectedTab == 0,
                    text = "Discover",
                    onClick = { selectedTab = 0 },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                )

                SegmentedTabItem(
                    selected = selectedTab == 1,
                    text = "Favorites",
                    onClick = { selectedTab = 1 },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(18.dp)
                        )
                    },
                    trailing = {
                        if (favoritesCount > 0) {
                            Text(
                                text = favoritesCount.toString(),
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
                                        shape = CircleShape
                                    )
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            when (selectedTab) {
                0 -> discover()
                else -> favorites()
            }
        }
    }
}

@Composable
private fun RowScope.SegmentedTabItem(
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailing: @Composable (() -> Unit)? = null,
) {
    val container = if (selected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
    val content = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = container),
        modifier = Modifier.weight(1f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.size(8.dp))
            }

            Text(
                text = text,
                color = content,
                style = MaterialTheme.typography.titleMedium,
            )

            if (trailing != null) {
                Spacer(modifier = Modifier.size(10.dp))
                trailing()
            }
        }
    }
}

