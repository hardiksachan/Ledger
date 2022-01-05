package com.hardiksachan.ledger.ui.features.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ScreenTitle(text: String, modifier: Modifier = Modifier, hasBack: Boolean = false) {
    val textModifier = if (hasBack) modifier else modifier.padding(top = 16.dp, bottom = 8.dp)
    Text(
        text = text,
        style = typography.h4,
        modifier = textModifier,
    )
}
