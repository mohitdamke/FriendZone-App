package com.example.friendzone.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf


@Composable
fun ColorsProvider(
    customColors: CustomColors,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalCustomColors provides customColors) {
        content()
    }
}
