package com.andrii_a.muze.ui.saved_items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.andrii_a.muze.ui.saved_items.components.FiltersBottomSheet
import com.andrii_a.muze.ui.saved_items.components.SavedItemsGrid
import kotlinx.coroutines.launch
import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.saved
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedItemsScreen(
    state: SavedItemsUiState,
    onEvent: (SavedItemsEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(Res.string.saved)) },
                actions = {
                    IconButton(onClick = { onEvent(SavedItemsEvent.OpenFilterDialog) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.Sort,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            SavedItemsGrid(
                savedItems = state.items,
                onSavedItemClick = { onEvent(SavedItemsEvent.SelectItem(it)) },
                onUnsaveItemClick = { onEvent(SavedItemsEvent.RemoveItem(it)) },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            )
        }
    }

    val scope = rememberCoroutineScope()

    if (state.isFilterDialogOpen) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(SavedItemsEvent.DismissFilterDialog) },
            sheetState = bottomSheetState
        ) {
            FiltersBottomSheet(
                savedItemsFilters = state.filters,
                onFiltersChanged = { filters -> onEvent(SavedItemsEvent.ChangeFilters(filters)) },
                onDismiss = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            onEvent(SavedItemsEvent.DismissFilterDialog)
                        }
                    }
                }
            )
        }
    }
}
