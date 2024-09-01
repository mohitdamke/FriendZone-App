package com.example.friendzone.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White

data class CustomColors(
    val primary: Color,
    val secondary: Color,
    val background: Color,
    val surface: Color
)

val defaultColor = CustomColors(
    primary = Color(0xFF66E082),
    secondary = Color(0xFF1AC0FF),
    background = Black,
    surface = White
)

val LocalCustomColors = staticCompositionLocalOf { defaultColor }