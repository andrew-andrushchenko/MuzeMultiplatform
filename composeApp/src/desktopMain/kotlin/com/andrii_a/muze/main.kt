package com.andrii_a.muze

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.andrii_a.muze.di.initKoin
import com.andrii_a.muze.ui.main.App

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Muze"
    ) {
        initKoin()
        App()
    }
}