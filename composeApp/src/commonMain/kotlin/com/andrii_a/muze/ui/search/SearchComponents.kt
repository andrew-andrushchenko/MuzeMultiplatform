package com.andrii_a.muze.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.outlined.SavedSearch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.andrii_a.muze.domain.models.RecentSearchItem
import com.andrii_a.muze.ui.common.EmptyContentBanner
import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.clear_recent_searches
import muzemultiplatform.composeapp.generated.resources.no_recent_searches
import org.jetbrains.compose.resources.stringResource

@Composable
fun RecentSearchesList(
    recentSearches: List<RecentSearchItem>,
    onItemSelected: (RecentSearchItem) -> Unit,
    onDeleteItem: (RecentSearchItem) -> Unit,
    onDeleteAllItems: () -> Unit
) {
    LazyColumn {
        if (recentSearches.isEmpty()) {
            item {
                EmptyContentBanner(
                    imageVector = Icons.Outlined.SavedSearch,
                    message = stringResource(Res.string.no_recent_searches),
                    modifier = Modifier.fillParentMaxSize()
                )
            }
        }

        items(
            count = recentSearches.size,
            key = { index -> recentSearches[index].id }
        ) { index ->
            val recentSearchItem = recentSearches[index]

            ListItem(
                headlineContent = { Text(text = recentSearchItem.title) },
                leadingContent = {
                    Icon(
                        Icons.Default.History,
                        contentDescription = null
                    )
                },
                trailingContent = {
                    IconButton(onClick = { onDeleteItem(recentSearchItem) }) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                modifier = Modifier
                    .clickable(onClick = { onItemSelected(recentSearchItem) })
                    .fillMaxWidth()
                    .padding(start = 16.dp)
            )
        }

        if (recentSearches.isNotEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    TextButton(
                        onClick = onDeleteAllItems,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Text(text = stringResource(Res.string.clear_recent_searches))
                    }
                }
            }
        }
    }
}