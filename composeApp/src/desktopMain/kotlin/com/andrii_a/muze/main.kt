package com.andrii_a.muze

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "MuzeMultiplatform",
    ) {
        App()
    }
}