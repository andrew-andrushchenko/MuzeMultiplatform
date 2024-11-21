package com.andrii_a.muze.ui.artwork_detail

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.ZoomInMap
import androidx.compose.material.icons.outlined.ZoomOutMap
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import coil3.compose.AsyncImagePainter
import coil3.compose.LocalPlatformContext
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import coil3.request.crossfade
import coil3.size.Size
import com.andrii_a.muze.domain.models.Artist
import com.andrii_a.muze.ui.artwork_detail.components.ArtistRow
import com.andrii_a.muze.ui.artwork_detail.components.BottomSheetContent
import com.andrii_a.muze.ui.artwork_detail.components.OverZoomConfig
import com.andrii_a.muze.ui.artwork_detail.components.Zoomable
import com.andrii_a.muze.ui.artwork_detail.components.rememberZoomableState
import com.andrii_a.muze.ui.common.ErrorBanner
import com.andrii_a.muze.ui.common.UiErrorWithRetry
import kotlinx.coroutines.launch
import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.error_loading_image
import muzemultiplatform.composeapp.generated.resources.navigate_back
import muzemultiplatform.composeapp.generated.resources.show_info_about_artwork
import muzemultiplatform.composeapp.generated.resources.zoom_to_fill
import org.jetbrains.compose.resources.stringResource
import kotlin.math.max

@Composable
fun ArtworkDetailScreen(
    state: ArtworkDetailUiState,
    onEvent: (ArtworkDetailEvent) -> Unit
) {
    when {
        state.isLoading -> {
            LoadingStateContent(
                onNavigateBack = { onEvent(ArtworkDetailEvent.GoBack) }
            )
        }

        !state.isLoading && state.error == null && state.artwork != null -> {
            SuccessStateContent(
                state = state,
                onEvent = onEvent
            )
        }

        else -> {
            val error = state.error as? UiErrorWithRetry
            //Toast.makeText(LocalContext.current, error?.reason?.asString(), Toast.LENGTH_SHORT).show()

            ErrorStateContent(
                onRetry = {
                    error?.onRetry?.invoke()
                },
                onNavigateBack = { onEvent(ArtworkDetailEvent.GoBack) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun LoadingStateContent(onNavigateBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(resource = Res.string.navigate_back),
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ErrorStateContent(
    onRetry: () -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(resource = Res.string.navigate_back),
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        ErrorBanner(
            onRetry = onRetry,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SuccessStateContent(
    state: ArtworkDetailUiState,
    onEvent: (ArtworkDetailEvent) -> Unit
) {
    val artwork = state.artwork!!

    val scope = rememberCoroutineScope()

    val bottomSheetState = rememberModalBottomSheetState()

    BoxWithConstraints(modifier = Modifier.background(color = Color.Black)) {
        val constraints = this

        var areControlsVisible by rememberSaveable { mutableStateOf(true) }
        var zoomToFillCoefficient by rememberSaveable { mutableFloatStateOf(1f) }

        val zoomableState = rememberZoomableState(
            minScale = 0.5f,
            maxScale = 6f,
            overZoomConfig = OverZoomConfig(1f, 4f)
        )

        Zoomable(
            state = zoomableState,
            enabled = true,
            onTap = { areControlsVisible = !areControlsVisible },
            dismissGestureEnabled = true,
            onDismiss = {
                onEvent(ArtworkDetailEvent.GoBack)
                true
            },
            modifier = Modifier.graphicsLayer {
                clip = true
                alpha = 1 - zoomableState.dismissDragProgress
            },
        ) {
            val painter = rememberAsyncImagePainter(
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data(artwork.image.url)
                    .size(Size.ORIGINAL)
                    .crossfade(durationMillis = 1000)
                    .build()
            )

            AnimatedContent(
                targetState = painter.state.value,
                label = "photo_content",
                transitionSpec = {
                    fadeIn() + scaleIn(animationSpec = tween(400)) togetherWith
                            fadeOut(animationSpec = tween(200))
                }
            ) { state ->
                when (state) {
                    AsyncImagePainter.State.Empty -> Unit
                    is AsyncImagePainter.State.Loading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    is AsyncImagePainter.State.Error -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            ErrorBanner(
                                message = stringResource(Res.string.error_loading_image),
                                onRetry = painter::restart
                            )
                        }
                    }

                    is AsyncImagePainter.State.Success -> {
                        val size = painter.intrinsicSize

                        Image(
                            painter = painter,
                            contentDescription = null,
                            modifier = Modifier
                                .aspectRatio(size.width / size.height)
                                .fillMaxSize()
                        )

                        zoomToFillCoefficient = getZoomToFillScaleCoefficient(
                            imageWidth = size.width,
                            imageHeight = size.height,
                            containerWidth = constraints.maxWidth.value,
                            containerHeight = constraints.maxHeight.value
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = areControlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .graphicsLayer {
                    alpha = 1 - zoomableState.dismissDragProgress
                }
        ) {
            TopSection(
                artworkName = artwork.name,
                isArtworkInSavedList = state.isArtworkInSavedList,
                onNavigateBack = { onEvent(ArtworkDetailEvent.GoBack) },
                onSaveArtwork = { onEvent(ArtworkDetailEvent.SaveArtWork(artwork)) },
                onRemoveArtworkFromSaved = {
                    onEvent(
                        ArtworkDetailEvent.RemoveArtworkFromSaved(
                            artwork
                        )
                    )
                }
            )
        }

        AnimatedVisibility(
            visible = areControlsVisible,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .graphicsLayer {
                    alpha = 1 - zoomableState.dismissDragProgress
                }
        ) {
            BottomSection(
                artist = artwork.artist,
                zoomIcon = if (zoomableState.scale == 1f) Icons.Outlined.ZoomOutMap else Icons.Outlined.ZoomInMap,
                onInfoButtonClick = { onEvent(ArtworkDetailEvent.ShowInfoDialog) },
                onZoomToFillClick = {
                    scope.launch {
                        zoomableState.animateScaleTo(
                            if (zoomableState.scale >= zoomToFillCoefficient) 1f
                            else zoomToFillCoefficient
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 8.dp)
            )
        }
    }

    if (state.isInfoDialogOpened) {
        ModalBottomSheet(
            onDismissRequest = { onEvent(ArtworkDetailEvent.DismissInfoDialog) },
            sheetState = bottomSheetState
        ) {
            BottomSheetContent(artwork = artwork)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSection(
    artworkName: String,
    isArtworkInSavedList: Boolean,
    onNavigateBack: () -> Unit,
    onSaveArtwork: () -> Unit,
    onRemoveArtworkFromSaved: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = artworkName,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = stringResource(resource = Res.string.navigate_back),
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    if (isArtworkInSavedList) {
                        onRemoveArtworkFromSaved()
                    } else {
                        onSaveArtwork()
                    }
                },
                modifier = Modifier.padding(8.dp).requiredSize(48.dp)
            ) {
                Icon(
                    imageVector = if (isArtworkInSavedList) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                    contentDescription = stringResource(Res.string.navigate_back)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black.copy(alpha = 0.3f),
            titleContentColor = Color.White,
            navigationIconContentColor = Color.White,
            actionIconContentColor = Color.White
        )
    )
}

@Composable
fun BottomSection(
    artist: Artist,
    zoomIcon: ImageVector,
    onInfoButtonClick: () -> Unit,
    onZoomToFillClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier) {
        val (artistRow, infoButton, zoomToFillButton) = createRefs()

        ArtistRow(
            artist = artist,
            modifier = Modifier.constrainAs(artistRow) {
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                top.linkTo(infoButton.top)
                bottom.linkTo(infoButton.bottom)
            }
        )

        IconButton(
            onClick = onInfoButtonClick,
            modifier = Modifier
                .constrainAs(infoButton) {
                    end.linkTo(zoomToFillButton.start, 8.dp)
                    top.linkTo(zoomToFillButton.top)
                    bottom.linkTo(zoomToFillButton.bottom)
                }
                .clip(RoundedCornerShape(32.dp))
                .background(Color.Black.copy(alpha = 0.55f))
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(resource = Res.string.show_info_about_artwork),
                tint = Color.White
            )
        }

        IconButton(
            onClick = onZoomToFillClick,
            modifier = Modifier
                .constrainAs(zoomToFillButton) {
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .clip(RoundedCornerShape(32.dp))
                .background(Color.Black.copy(alpha = 0.55f))
        ) {
            Icon(
                imageVector = zoomIcon,
                contentDescription = stringResource(resource = Res.string.zoom_to_fill),
                tint = Color.White
            )
        }
    }
}

@Composable
private fun getZoomToFillScaleCoefficient(
    imageWidth: Float,
    imageHeight: Float,
    containerWidth: Float,
    containerHeight: Float
): Float {
    val widthRatio = imageWidth / imageHeight
    val height = containerWidth / widthRatio
    val zoomScaleH = containerHeight / height

    val heightRatio = imageHeight / imageWidth
    val width = containerHeight / heightRatio
    val zoomScaleW = containerWidth / width

    return max(zoomScaleW, zoomScaleH)
}
