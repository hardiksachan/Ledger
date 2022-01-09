package com.hardiksachan.ledger.ui.features.instruments.add

import androidx.compose.animation.Animatable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hardiksachan.ledger.R
import com.hardiksachan.ledger.presentation_logic.instruments.add.AddInstrumentEvent
import com.hardiksachan.ledger.presentation_logic.instruments.add.AddInstrumentViewModel
import com.hardiksachan.ledger.ui.base.scopednav.navigation.NoParams
import com.hardiksachan.ledger.ui.base.scopednav.navigation.ScreenDestination
import com.hardiksachan.ledger.ui.features.widgets.ScreenTitle
import com.hardiksachan.ledger.ui.utils.toColor

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
        topBar =  {
            ScreenTitle(
                text = stringResource(R.string.add_instrument_screen_title),
                modifier = Modifier.padding(16.dp)
            )
        },
    ) {

    }

}