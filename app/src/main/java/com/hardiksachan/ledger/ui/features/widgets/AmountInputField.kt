package com.hardiksachan.ledger.ui.features.widgets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hardiksachan.ledger.ui.theme.ledgerTextFieldColors
import com.hardiksachan.ledger.ui.utils.toRupee

@Composable
fun AmountInputField(
    amount: Long,
    onAmountChanged: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Change Rupees
        OutlinedTextField(
            value = amount.toRupee().split(".")[0].dropWhile { it == '0' },
            onValueChange = {
                val sanitised = it.toLongOrNull() ?: 0L
                onAmountChanged(
                    sanitised * 100 + amount.toRupee().split(".")[1].toLong()
                )
            },
            label = {
                Text(text = "Rs")
            },
            modifier = Modifier.fillMaxWidth(.75f),
            colors = ledgerTextFieldColors(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )

        Spacer(modifier = Modifier.width(8.dp))

        // Change Paise
        OutlinedTextField(
            value = amount.toRupee().split(".")[1].dropWhile { it == '0' },
            onValueChange = {
                val sanitised = it.take(2).toLongOrNull() ?: 0L
                onAmountChanged(
                    amount.toRupee().split(".")[0].toLong() * 100 + sanitised
                )
            },
            label = {
                Text(text = "p")
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ledgerTextFieldColors(),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
    }
}

@Preview
@Composable
fun AmountInputFieldPreview() {
    AmountInputField(amount = 20000050, onAmountChanged = { })
}