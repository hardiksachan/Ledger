package com.hardiksachan.ledger.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Color.White,
    background = DarkGray,
    onBackground = Color.White,
    surface = LightBlue,
    onSurface = DarkGray
)

@Composable
fun LedgerTheme(darkTheme: Boolean = true, content: @Composable() () -> Unit) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

@Composable
fun ledgerTextFieldColors() =
    TextFieldDefaults.outlinedTextFieldColors(
        focusedBorderColor = MaterialTheme.colors.background,
        unfocusedBorderColor = MaterialTheme.colors.background,
        textColor = MaterialTheme.colors.background,
        focusedLabelColor = MaterialTheme.colors.background,
        unfocusedLabelColor = MaterialTheme.colors.background,
        cursorColor = MaterialTheme.colors.background
    )