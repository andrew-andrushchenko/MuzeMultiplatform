package com.andrii_a.muze.ui.saved_items.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.andrii_a.muze.domain.util.SortOrder
import com.andrii_a.muze.ui.util.titleRes
import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.apply
import muzemultiplatform.composeapp.generated.resources.display_item_types
import muzemultiplatform.composeapp.generated.resources.sort_order
import org.jetbrains.compose.resources.stringResource

@Composable
fun FiltersBottomSheet(
    savedItemsFilters: SavedItemsFilters,
    onFiltersChanged: (SavedItemsFilters) -> Unit,
    onDismiss: () -> Unit,
    contentPadding: PaddingValues = PaddingValues()
) {
    var sortOrder by rememberSaveable {
        mutableStateOf(savedItemsFilters.sortOrder)
    }

    var displayItemTypes by rememberSaveable {
        mutableStateOf(savedItemsFilters.displayItemTypes)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(contentPadding)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(Res.string.sort_order),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            val options = SortOrder.entries

            options.forEachIndexed { index, tabPage ->
                SegmentedButton(
                    selected = sortOrder == tabPage,
                    onClick = { sortOrder = tabPage },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    label = {
                        Text(
                            text = stringResource(tabPage.titleRes),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(Res.string.display_item_types),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            val options = DisplayItemTypes.entries

            options.forEachIndexed { index, tabPage ->
                SegmentedButton(
                    selected = displayItemTypes == tabPage,
                    onClick = { displayItemTypes = tabPage },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = options.size
                    ),
                    label = {
                        Text(
                            text = stringResource(tabPage.titleRes),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val filters = SavedItemsFilters(
                    sortOrder = sortOrder,
                    displayItemTypes = displayItemTypes
                )

                onFiltersChanged(filters)
                onDismiss()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = stringResource(Res.string.apply),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}