package com.andrii_a.muze.ui.saved_items.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkRemove
import androidx.compose.material.icons.outlined.CollectionsBookmark
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.andrii_a.muze.domain.models.SavedItem
import com.andrii_a.muze.ui.common.EmptyContentBanner
import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.artist_portrait
import muzemultiplatform.composeapp.generated.resources.no_saved_items
import org.jetbrains.compose.resources.stringResource

@Composable
fun SavedItemsGrid(
    savedItems: List<SavedItem>,
    onSavedItemClick: (SavedItem) -> Unit,
    onUnsaveItemClick: (SavedItem) -> Unit,
    modifier: Modifier = Modifier,
    gridState: LazyGridState = rememberLazyGridState(),
    contentPadding: PaddingValues = PaddingValues()
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Adaptive(150.dp),
            state = gridState,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = contentPadding,
            modifier = modifier
        ) {
            if (savedItems.isNotEmpty()) {
                items(
                    items = savedItems,
                    key = { it.timestampMillis }
                ) { item ->
                    SavedListItem(
                        savedItem = item,
                        onClick = { onSavedItemClick(item) },
                        onUnsaveClick = { onUnsaveItemClick(item) },
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }

        if (savedItems.isEmpty()) {
            EmptyContentBanner(
                imageVector = Icons.Outlined.CollectionsBookmark,
                message = stringResource(Res.string.no_saved_items),
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun SavedListItem(
    savedItem: SavedItem,
    onClick: () -> Unit,
    onUnsaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        ConstraintLayout {
            val (image, title, unsaveButton) = createRefs()

            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(savedItem.imageUrl)
                    .crossfade(durationMillis = 1000)
                    .scale(Scale.FILL)
                    //.placeholder(Color.Gray.toArgb())
                    .build(),
                contentDescription = stringResource(resource = Res.string.artist_portrait),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .aspectRatio(1f)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Text(
                text = savedItem.title,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(title) {
                    start.linkTo(parent.start, 8.dp)
                    end.linkTo(unsaveButton.start, 8.dp)
                    top.linkTo(image.bottom, 8.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                    width = Dimension.fillToConstraints
                }
            )

            FilledTonalIconButton(
                onClick = onUnsaveClick,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                ),
                modifier = Modifier.constrainAs(unsaveButton) {
                    top.linkTo(image.bottom, 8.dp)
                    bottom.linkTo(parent.bottom, 8.dp)
                    end.linkTo(parent.end)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.BookmarkRemove,
                    contentDescription = null
                )
            }
        }
    }
}