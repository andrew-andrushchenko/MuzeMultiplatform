package com.andrii_a.muze.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Brush
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Search
import androidx.compose.ui.graphics.vector.ImageVector
import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.artists
import muzemultiplatform.composeapp.generated.resources.artworks
import muzemultiplatform.composeapp.generated.resources.saved
import muzemultiplatform.composeapp.generated.resources.search
import org.jetbrains.compose.resources.StringResource

enum class NavigationScreen(
    val screen: Screen,
    val titleRes: StringResource,
    val iconUnselected: ImageVector,
    val iconSelected: ImageVector
) {
    Artists(
        screen = Screen.Artists,
        titleRes = Res.string.artists,
        iconSelected = Icons.Filled.People,
        iconUnselected = Icons.Outlined.Groups
    ),
    Artworks(
        screen = Screen.Artworks,
        titleRes = Res.string.artworks,
        iconSelected = Icons.Filled.Brush,
        iconUnselected = Icons.Outlined.Brush
    ),
    Search(
        screen = Screen.Search,
        titleRes = Res.string.search,
        iconSelected = Icons.Filled.Search,
        iconUnselected = Icons.Outlined.Search
    ),
    Saved(
        screen = Screen.Saved,
        titleRes = Res.string.saved,
        iconSelected = Icons.Default.Bookmark,
        iconUnselected = Icons.Outlined.BookmarkBorder
    )
}
