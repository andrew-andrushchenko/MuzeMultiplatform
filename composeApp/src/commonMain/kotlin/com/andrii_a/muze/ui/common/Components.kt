package com.andrii_a.muze.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.outlined.CloudOff
import androidx.compose.material.icons.outlined.ImageSearch
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import app.cash.paging.compose.LazyPagingItems
import app.cash.paging.compose.itemKey
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.andrii_a.muze.domain.models.Artist
import com.andrii_a.muze.domain.models.Artwork
import com.andrii_a.muze.ui.theme.BottleCapShape
import com.andrii_a.muze.ui.theme.CloverShape
import com.andrii_a.muze.ui.util.lifeYearsString
import kotlinx.coroutines.launch
import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.artist_portrait
import muzemultiplatform.composeapp.generated.resources.artwork_item
import muzemultiplatform.composeapp.generated.resources.by_formatted
import muzemultiplatform.composeapp.generated.resources.empty_content_banner_text
import muzemultiplatform.composeapp.generated.resources.error_banner_text
import muzemultiplatform.composeapp.generated.resources.error_loading_items
import muzemultiplatform.composeapp.generated.resources.retry
import muzemultiplatform.composeapp.generated.resources.to_top
import org.jetbrains.compose.resources.stringResource

@Composable
expect fun PlatformBackHandler(enabled: Boolean, onNavigateBack: () -> Unit)

@Composable
fun AspectRatioImage(
    width: Float,
    height: Float,
    painter: Painter,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(16.dp),
    clickable: Boolean = true,
    onClick: () -> Unit = {}
) {
    Box(modifier = modifier) {
        val aspectRatio = width / height

        Image(
            painter = painter,
            contentDescription = stringResource(resource = Res.string.artwork_item),
            contentScale = ContentScale.Fit,
            modifier = Modifier.then(
                if (clickable) {
                    Modifier.aspectRatio(aspectRatio)
                        .fillMaxWidth()
                        .clip(shape)
                        .clickable(onClick = onClick)
                } else {
                    Modifier
                        .aspectRatio(aspectRatio)
                        .fillMaxWidth()
                        .clip(shape)
                }
            )
        )
    }
}

@Composable
fun ScrollToTopLayout(
    listState: LazyListState,
    scrollToTopButtonPadding: PaddingValues = PaddingValues(),
    list: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    Box {
        list()

        val showButton = remember {
            derivedStateOf {
                listState.firstVisibleItemIndex > 0
            }
        }

        AnimatedVisibility(
            visible = showButton.value,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(scrollToTopButtonPadding)
        ) {
            ExtendedFloatingActionButton(
                text = {
                    Text(text = stringResource(resource = Res.string.to_top))
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = stringResource(resource = Res.string.to_top)
                    )
                },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary,
                onClick = {
                    scope.launch {
                        listState.scrollToItem(0)
                    }
                }
            )
        }
    }
}

@Composable
fun ScrollToTopLayout(
    gridState: LazyStaggeredGridState,
    scrollToTopButtonPadding: PaddingValues = PaddingValues(),
    grid: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()

    Box {
        grid()

        val showButton = remember {
            derivedStateOf {
                gridState.firstVisibleItemIndex > 0
            }
        }

        AnimatedVisibility(
            visible = showButton.value,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(scrollToTopButtonPadding)
        ) {
            ExtendedFloatingActionButton(
                text = {
                    Text(text = stringResource(resource = Res.string.to_top))
                },
                icon = {
                    Icon(
                        imageVector = Icons.Default.ArrowUpward,
                        contentDescription = stringResource(resource = Res.string.to_top)
                    )
                },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary,
                onClick = {
                    scope.launch {
                        gridState.scrollToItem(0)
                    }
                }
            )
        }
    }
}

@Composable
fun LoadingListItem(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
fun EmptyContentBanner(
    modifier: Modifier = Modifier,
    imageVector: ImageVector? = Icons.Outlined.ImageSearch,
    message: String = stringResource(resource = Res.string.empty_content_banner_text)
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        imageVector?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = message,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}

@Composable
fun ErrorBanner(
    modifier: Modifier = Modifier,
    message: String = stringResource(resource = Res.string.error_banner_text),
    onRetry: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Outlined.CloudOff,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(72.dp)
        )

        Text(
            text = message,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.padding(vertical = 8.dp))

        Button(onClick = onRetry) {
            Text(text = stringResource(resource = Res.string.retry))
        }
    }
}

@Composable
fun ErrorItem(
    modifier: Modifier = Modifier,
    message: String = stringResource(resource = Res.string.error_loading_items),
    onRetry: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = message,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(0.6f)
            )

            Button(
                onClick = onRetry,
                modifier = Modifier.weight(0.2f)
            ) {
                Text(text = stringResource(resource = Res.string.retry))
            }
        }
    }
}

@Composable
fun ArtworksStaggeredGrid(
    lazyArtworkItems: LazyPagingItems<Artwork>,
    onArtworkClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    gridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    contentPadding: PaddingValues = PaddingValues()
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(250.dp),
            state = gridState,
            modifier = modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalItemSpacing = 8.dp,
            contentPadding = contentPadding
        ) {
            if (lazyArtworkItems.loadState.refresh is LoadState.NotLoading && lazyArtworkItems.itemCount > 0) {
                items(
                    count = lazyArtworkItems.itemCount,
                    key = lazyArtworkItems.itemKey { it.id }
                ) { index ->
                    val artwork = lazyArtworkItems[index]
                    artwork?.let {
                        DefaultArtworkItem(
                            artwork = artwork,
                            onArtworkClick = { onArtworkClick(artwork.id) },
                            modifier = Modifier.animateItem()
                        )
                    }
                }
            }
            /*when (lazyArtworkItems.loadState.refresh) {
                is LoadState.NotLoading -> {
                    if (lazyArtworkItems.itemCount > 0) {
                        items(
                            count = lazyArtworkItems.itemCount,
                            key = lazyArtworkItems.itemKey { it.id }
                        ) { index ->
                            val artwork = lazyArtworkItems[index]
                            artwork?.let {
                                DefaultArtworkItem(
                                    artwork = artwork,
                                    onArtworkClick = { onArtworkClick(artwork.id) },
                                    modifier = Modifier.animateItem()
                                )
                            }
                        }
                    }/* else {
                        item(span = StaggeredGridItemSpan.FullLine) {
                            EmptyContentBanner(modifier = Modifier.fillMaxSize())
                        }
                    }*/
                }

                is LoadState.Loading -> {
                    Unit
                    /*item(span = StaggeredGridItemSpan.FullLine) {
                        LoadingListItem(modifier = Modifier.fillMaxSize())
                    }*/
                }

                is LoadState.Error -> {
                    Unit
                    /*item(span = StaggeredGridItemSpan.FullLine) {
                        ErrorBanner(
                            onRetry = lazyArtworkItems::retry,
                            modifier = Modifier.fillMaxSize()
                        )
                    }*/
                }
            }*/

            when (lazyArtworkItems.loadState.append) {
                is LoadState.NotLoading -> Unit

                is LoadState.Loading -> {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        LoadingListItem(modifier = Modifier.fillMaxWidth())
                    }
                }

                is LoadState.Error -> {
                    item(span = StaggeredGridItemSpan.FullLine) {
                        ErrorItem(
                            onRetry = lazyArtworkItems::retry,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        )
                    }
                }
            }
        }

        if (lazyArtworkItems.loadState.refresh is LoadState.Error) {
            ErrorBanner(
                onRetry = lazyArtworkItems::retry,
                modifier = Modifier.fillMaxSize()
            )
        }

        if (lazyArtworkItems.loadState.refresh is LoadState.Loading) {
            LoadingListItem(modifier = Modifier.fillMaxSize())
        }

        val shouldShowEmptyContent = lazyArtworkItems.loadState.refresh is LoadState.NotLoading
                && lazyArtworkItems.itemCount == 0

        if (shouldShowEmptyContent) {
            EmptyContentBanner(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
private fun DefaultArtworkItem(
    artwork: Artwork,
    onArtworkClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        onClick = onArtworkClick,
        modifier = modifier
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (artworkImage, artworkNameText, descriptionText, artistName, yearText) = createRefs()

            AspectRatioImage(
                width = artwork.image.width.toFloat(),
                height = artwork.image.height.toFloat(),
                painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalPlatformContext.current)
                        .data(artwork.image.url)
                        .crossfade(durationMillis = 1000)
                        .scale(Scale.FILL)
                        //.placeholder(ColorDrawable(android.graphics.Color.GRAY))
                        .build()
                ),
                clickable = false,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                modifier = Modifier
                    .constrainAs(artworkImage) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            Text(
                text = artwork.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(artworkNameText) {
                    top.linkTo(artworkImage.bottom, 8.dp)
                    bottom.linkTo(artistName.top, 0.dp)
                    start.linkTo(parent.start, 12.dp)
                    end.linkTo(yearText.start, 8.dp)
                    width = Dimension.fillToConstraints
                }
            )

            Text(
                text = stringResource(resource = Res.string.by_formatted, artwork.artist.name),
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.constrainAs(artistName) {
                    start.linkTo(parent.start, 12.dp)
                    end.linkTo(parent.end, 12.dp)
                    if (!artwork.description.isNullOrBlank()) {
                        bottom.linkTo(descriptionText.top, 8.dp)
                    } else {
                        bottom.linkTo(parent.bottom, 8.dp)
                    }
                    width = Dimension.fillToConstraints
                }
            )

            if (!artwork.description.isNullOrBlank()) {
                Text(
                    text = artwork.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.constrainAs(descriptionText) {
                        start.linkTo(parent.start, 12.dp)
                        end.linkTo(parent.end, 12.dp)
                        bottom.linkTo(parent.bottom, 8.dp)
                        width = Dimension.fillToConstraints
                    }
                )
            }

            if (!artwork.year.isNullOrEmpty()) {
                YearIndicatorText(
                    yearStr = artwork.year,
                    modifier = Modifier.constrainAs(yearText) {
                        top.linkTo(artworkNameText.top, 8.dp)
                        bottom.linkTo(artworkNameText.bottom, 8.dp)
                        end.linkTo(parent.end, 8.dp)
                    }
                )
            }
        }
    }
}

@Composable
private fun YearIndicatorText(
    yearStr: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = yearStr,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
        color = MaterialTheme.colorScheme.onPrimary,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = MaterialTheme.colorScheme.primary)
            .padding(horizontal = 16.dp, vertical = 6.dp)
    )
}

@Composable
fun ArtworksGridContent(
    artworkItems: LazyPagingItems<Artwork>,
    gridState: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    contentPadding: PaddingValues = PaddingValues(),
    scrollToTopButtonPadding: PaddingValues = PaddingValues(
        bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 8.dp
    ),
    onArtworkClick: (artworkId: Int) -> Unit
) {
    ScrollToTopLayout(
        gridState = gridState,
        scrollToTopButtonPadding = scrollToTopButtonPadding
    ) {
        ArtworksStaggeredGrid(
            lazyArtworkItems = artworkItems,
            onArtworkClick = onArtworkClick,
            gridState = gridState,
            contentPadding = PaddingValues(
                top = contentPadding.calculateTopPadding() + 16.dp,
                bottom = contentPadding.calculateBottomPadding() + 150.dp,
                start = 16.dp,
                end = 16.dp
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ArtistsListContent(
    artistItems: LazyPagingItems<Artist>,
    listState: LazyListState = rememberLazyListState(),
    selectedArtist: Artist? = null,
    contentPadding: PaddingValues = PaddingValues(),
    scrollToTopButtonPadding: PaddingValues = PaddingValues(
        bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding() + 8.dp
    ),
    onArtistClick: (Artist) -> Unit
) {
    ScrollToTopLayout(
        listState = listState,
        scrollToTopButtonPadding = scrollToTopButtonPadding
    ) {
        ArtistsList(
            artistItems = artistItems,
            onArtistClick = onArtistClick,
            listState = listState,
            selectedArtist = selectedArtist,
            contentPadding = contentPadding
        )
    }
}

@Composable
fun ArtistsList(
    artistItems: LazyPagingItems<Artist>,
    onArtistClick: (Artist) -> Unit,
    selectedArtist: Artist? = null,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues()
) {
    LazyColumn(
        state = listState,
        contentPadding = contentPadding,
        modifier = modifier
    ) {
        when (artistItems.loadState.refresh) {
            is LoadState.NotLoading -> {
                if (artistItems.itemCount > 0) {
                    items(
                        count = artistItems.itemCount,
                        key = artistItems.itemKey { it.id }
                    ) { index ->
                        val artist = artistItems[index]
                        artist?.let {
                            ArtistItem(
                                artist = artist,
                                onClick = onArtistClick,
                                isSelected = selectedArtist?.id == artist.id,
                                artistPhotoShape = if (index % 2 == 0) BottleCapShape else CloverShape
                            )
                        }
                    }
                } else {
                    item {
                        EmptyContentBanner(modifier = Modifier.fillParentMaxSize())
                    }
                }
            }

            is LoadState.Loading -> {
                item {
                    LoadingListItem(modifier = Modifier.fillParentMaxSize())
                }
            }

            is LoadState.Error -> {
                item {
                    ErrorBanner(
                        onRetry = artistItems::retry,
                        modifier = Modifier.fillParentMaxSize()
                    )
                }
            }
        }

        when (artistItems.loadState.append) {
            is LoadState.NotLoading -> Unit

            is LoadState.Loading -> {
                item {
                    LoadingListItem(modifier = Modifier.fillParentMaxWidth())
                }
            }

            is LoadState.Error -> {
                item {
                    ErrorItem(
                        onRetry = artistItems::retry,
                        modifier = Modifier
                            .fillParentMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ArtistItem(
    artist: Artist,
    onClick: (Artist) -> Unit,
    isSelected: Boolean,
    artistPhotoShape: Shape = CircleShape,
    itemShape: Shape = RoundedCornerShape(16.dp),
    modifier: Modifier = Modifier
) {
    val elevation by animateDpAsState(
        targetValue = if (isSelected) 1.dp else 0.dp,
        label = "tonalItemElevation"
    )

    val animatedPadding by animateDpAsState(
        targetValue = if (isSelected) 8.dp else 0.dp,
        label = "padding"
    )

    ListItem(
        headlineContent = {
            Text(
                text = artist.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            AsyncImage(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(artist.portraitImage.url)
                    .crossfade(durationMillis = 1000)
                    .scale(Scale.FILL)
                    //.placeholder(Color.Gray.toArgb())
                    .build(),
                contentDescription = stringResource(resource = Res.string.artist_portrait),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(artistPhotoShape)
            )
        },
        supportingContent = {
            Text(
                text = artist.lifeYearsString,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null
            )
        },
        tonalElevation = elevation,
        modifier = modifier
            .padding(animatedPadding)
            .clip(itemShape)
            .then(
                if (isSelected) {
                    Modifier
                } else {
                    Modifier.clickable { onClick(artist) }
                }
            )
    )
}

@Composable
fun SmallArtworkItem(
    imageUrl: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalPlatformContext.current)
            .data(imageUrl)
            .crossfade(durationMillis = 1000)
            .scale(Scale.FILL)
            //.placeholder(ColorDrawable(Color.GRAY))
            .build(),
        contentDescription = stringResource(Res.string.artwork_item),
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(128.dp)
            .clip(CloverShape)
            .clip(RoundedCornerShape(24.dp))
            .clickable(onClick = onClick)
    )
}
