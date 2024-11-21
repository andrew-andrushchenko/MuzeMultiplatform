package com.andrii_a.muze.ui.artists

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import app.cash.paging.compose.LazyPagingItems
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Scale
import com.andrii_a.muze.domain.models.Artist
import com.andrii_a.muze.domain.models.Artwork
import com.andrii_a.muze.ui.common.ArtistsListContent
import com.andrii_a.muze.ui.common.SmallArtworkItem
import com.andrii_a.muze.ui.theme.BottleCapShape
import com.andrii_a.muze.ui.util.lifeYearsString
import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.artist_portrait
import muzemultiplatform.composeapp.generated.resources.artists
import muzemultiplatform.composeapp.generated.resources.artworks_by_formatted
import muzemultiplatform.composeapp.generated.resources.navigate_back
import muzemultiplatform.composeapp.generated.resources.show_all_artworks_by_artist
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistListPane(
    lazyArtistItems: LazyPagingItems<Artist>,
    onArtistClick: (Artist) -> Unit,
    selectedArtist: Artist? = null,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = stringResource(Res.string.artists)) })
        },
        modifier = modifier
    ) { innerPadding ->
        val listState = rememberLazyListState()

        ArtistsListContent(
            artistItems = lazyArtistItems,
            listState = listState,
            onArtistClick = onArtistClick,
            selectedArtist = selectedArtist,
            contentPadding = PaddingValues(
                top = innerPadding.calculateTopPadding(),
                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                bottom = innerPadding.calculateBottomPadding() + WindowInsets.systemBars.asPaddingValues()
                    .calculateBottomPadding(),
            ),
            scrollToTopButtonPadding = PaddingValues(bottom = 8.dp)
        )
    }
}

@Composable
fun ArtistDetailPane(
    artist: Artist,
    isArtistInSavedList: Boolean,
    lazyArtworkItems: LazyPagingItems<Artwork>,
    tonalElevation: Dp = 1.dp,
    navigateBack: () -> Unit,
    onSaveArtist: (Artist) -> Unit,
    onRemoveArtistFromSaved: (Artist) -> Unit,
    onArtworkSelect: (Int) -> Unit,
    onMoreArtworksClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = tonalElevation,
        modifier = modifier.statusBarsPadding()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            ArtistDetailPaneTopPanel(
                artist = artist,
                isArtistInSavedList = isArtistInSavedList,
                navigateBack = navigateBack,
                onSaveArtist = onSaveArtist,
                onRemoveArtistFromSaved = onRemoveArtistFromSaved,
                modifier = Modifier.height(64.dp).fillMaxWidth()
            )

            ArtistDetailContent(
                artist = artist,
                lazyArtworkItems = lazyArtworkItems,
                onArtworkSelect = onArtworkSelect,
                onMoreArtworksClick = onMoreArtworksClick
            )
        }
    }
}

@Composable
fun ArtistDetailPane(
    artist: Artist,
    lazyArtworkItems: LazyPagingItems<Artwork>,
    tonalElevation: Dp = 1.dp,
    onArtworkSelect: (Int) -> Unit,
    onMoreArtworksClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = tonalElevation,
        modifier = modifier
    ) {
        ArtistDetailContent(
            artist = artist,
            lazyArtworkItems = lazyArtworkItems,
            onArtworkSelect = onArtworkSelect,
            onMoreArtworksClick = onMoreArtworksClick
        )
    }
}

@Composable
private fun ArtistDetailContent(
    artist: Artist,
    lazyArtworkItems: LazyPagingItems<Artwork>,
    onArtworkSelect: (Int) -> Unit,
    onMoreArtworksClick: () -> Unit
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(artist.portraitImage.url)
                .crossfade(durationMillis = 1000)
                .scale(Scale.FILL)
                //.placeholder(ColorDrawable(Color.GRAY))
                .build(),
            contentDescription = stringResource(resource = Res.string.artist_portrait),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(250.dp)
                .clip(BottleCapShape)
                .clip(CircleShape)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = artist.name,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = artist.lifeYearsString,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .widthIn(max = 800.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            var expanded by remember {
                mutableStateOf(false)
            }

            Text(
                text = artist.bio,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (expanded) Int.MAX_VALUE else 12,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable { expanded = !expanded }
                    .animateContentSize()
                    .padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(
                resource = Res.string.artworks_by_formatted,
                artist.name
            ),
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 18.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        val artworksSnapshotList = lazyArtworkItems.itemSnapshotList.take(8)
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(count = artworksSnapshotList.size) { index ->
                val artwork = artworksSnapshotList[index]
                artwork?.let {
                    SmallArtworkItem(
                        imageUrl = artwork.image.url,
                        onClick = { onArtworkSelect(artwork.id) },
                        modifier = Modifier.padding(
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 16.dp
                        )
                    )
                }
            }

            item {
                FloatingActionButton(
                    onClick = onMoreArtworksClick,
                    elevation = FloatingActionButtonDefaults.elevation(0.dp),
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                    content = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowForward,
                            contentDescription = stringResource(resource = Res.string.show_all_artworks_by_artist)
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun ArtistDetailPaneTopPanel(
    artist: Artist,
    isArtistInSavedList: Boolean,
    navigateBack: () -> Unit,
    onSaveArtist: (Artist) -> Unit,
    onRemoveArtistFromSaved: (Artist) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        IconButton(
            onClick = navigateBack,
            modifier = Modifier.padding(8.dp).requiredSize(48.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = stringResource(Res.string.navigate_back)
            )
        }

        IconButton(
            onClick = {
                if (isArtistInSavedList) {
                    onRemoveArtistFromSaved(artist)
                } else {
                    onSaveArtist(artist)
                }
            },
            modifier = Modifier.padding(8.dp).requiredSize(48.dp)
        ) {
            Icon(
                imageVector = if (isArtistInSavedList) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                contentDescription = stringResource(Res.string.navigate_back)
            )
        }
    }
}