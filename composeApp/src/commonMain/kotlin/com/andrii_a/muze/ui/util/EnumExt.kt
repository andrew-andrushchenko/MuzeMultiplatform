package com.andrii_a.muze.ui.util

import com.andrii_a.muze.domain.util.SortOrder
import muzemultiplatform.composeapp.generated.resources.Res
import muzemultiplatform.composeapp.generated.resources.sort_order_date_saved
import muzemultiplatform.composeapp.generated.resources.sort_order_title
import org.jetbrains.compose.resources.StringResource

val SortOrder.titleRes: StringResource
    get() = when (this) {
        SortOrder.BY_TITLE -> Res.string.sort_order_title
        SortOrder.BY_DATE_SAVED -> Res.string.sort_order_date_saved
    }