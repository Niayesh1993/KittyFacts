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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.zozi.kittyfacts.R
import com.zozi.kittyfacts.presentation.theme.KittyDimens

/**
 * Simple 2-tab container (Discover / Favorites)
 * This is intentionally lightweight and doesn't depend on Navigation.
 */
@Composable
fun KittyFactsHome(
    modifier: Modifier = Modifier,
    favoritesCount: Int = 0,
    discover: @Composable () -> Unit,
    favorites: @Composable () -> Unit,
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    Surface(modifier = modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(KittyDimens.space16)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(KittyDimens.space12), modifier = Modifier.fillMaxWidth()) {
                SegmentedTabItem(
                    selected = selectedTab == 0,
                    text = stringResource(R.string.tab_discover),
                    onClick = { selectedTab = 0 },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(KittyDimens.icon18)
                        )
                    }
                )

                SegmentedTabItem(
                    selected = selectedTab == 1,
                    text = stringResource(R.string.tab_favorites),
                    onClick = { selectedTab = 1 },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Favorite,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(KittyDimens.icon18)
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
                                    .padding(horizontal = KittyDimens.space10, vertical = KittyDimens.space4)
                            )
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(KittyDimens.space16))

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
                .padding(horizontal = KittyDimens.space16, vertical = KittyDimens.space12),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (leadingIcon != null) {
                leadingIcon()
                Spacer(modifier = Modifier.size(KittyDimens.space8))
            }

            Text(
                text = text,
                color = content,
                style = MaterialTheme.typography.titleMedium,
            )

            if (trailing != null) {
                Spacer(modifier = Modifier.size(KittyDimens.space10))
                trailing()
            }
        }
    }
}
