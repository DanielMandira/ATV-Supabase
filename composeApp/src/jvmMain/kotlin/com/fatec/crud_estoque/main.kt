package com.fatec.crud_estoque

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "CRUD_Estoque",
    ) {
        App()
    }
}