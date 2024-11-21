package com.andrii_a.muze.ui.saved_items.components

import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.artists
import muzemultiplatform.composeapp.generated.resources.artworks
import muzemultiplatform.composeapp.generated.resources.display_item_types_all
import org.jetbrains.compose.resources.StringResource

enum class DisplayItemTypes(val titleRes: StringResource) {
    ALL(Res.string.display_item_types_all),
    ARTISTS(Res.string.artists),
    ARTWORKS(Res.string.artworks)
}