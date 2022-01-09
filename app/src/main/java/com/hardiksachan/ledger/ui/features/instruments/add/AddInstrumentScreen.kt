package com.hardiksachan.ledger.ui.features.instruments.add

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.hardiksachan.ledger.R
import com.hardiksachan.ledger.presentation_logic.instruments.add.AddInstrumentEvent
import com.hardiksachan.ledger.presentation_logic.instruments.add.AddInstrumentViewModel
import com.hardiksachan.ledger.ui.base.scopednav.navigation.NoParams
import com.hardiksachan.ledger.ui.base.scopednav.navigation.ScreenDestination
import com.hardiksachan.ledger.ui.features.widgets.AmountInputField
import com.hardiksachan.ledger.ui.features.widgets.ColorChooser
import com.hardiksachan.ledger.ui.features.widgets.ScreenTitleWithIcon
import com.hardiksachan.ledger.ui.theme.ledgerTextFieldColors
import com.hardiksachan.ledger.ui.utils.toColor
import kotlinx.coroutines.launch

object AddInstrumentScreen : ScreenDestination<NoParams>(pathRoot = "addInstrumentScreen")

@Composable
fun AddInstrumentScreen(
    vm: AddInstrumentViewModel,
    bottomBar: @Composable () -> Unit
) {
    val name = vm.name.collectAsState()
    val openingBalance = vm.openingBalance.collectAsState()
    val color = vm.color.collectAsState()

    val backgroundAnimatable = remember {
        Animatable(color.value.toColor())
    }

    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    vm.onEvent(AddInstrumentEvent.OnSavePressed)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(Icons.Filled.Done, "Save instrument")
            }
        },
        scaffoldState = scaffoldState,
        bottomBar = { bottomBar() },
        topBar = {
            Row {
                ScreenTitleWithIcon(
                    text = stringResource(R.string.add_instrument_screen_title),
                    modifier = Modifier.padding(16.dp),
                    Icon = {
                        Icon(Icons.Filled.Close, contentDescription = "Cancel")
                    }
                ) {
                    vm.onEvent(AddInstrumentEvent.OnCancelPressed)
                }
            }
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundAnimatable.value)
                .padding(16.dp)
        ) {
            ColorChooser(color.value) { tagColor ->
                scope.launch {
                    backgroundAnimatable.animateTo(
                        targetValue = tagColor.toColor(),
                        animationSpec = tween(
                            durationMillis = 500
                        )
                    )
                }
                vm.onEvent(AddInstrumentEvent.OnColorChanged(tagColor))
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name.value,
                onValueChange = {
                    vm.onEvent(AddInstrumentEvent.OnNameChanged(it))
                },
                label = {
                    Text(text = "Instrument Name")
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ledgerTextFieldColors(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            AmountInputField(amount = openingBalance.value, onAmountChanged = {
                vm.onEvent(AddInstrumentEvent.OnOpeningBalanceChanged(it))
            })
        }
    }

}