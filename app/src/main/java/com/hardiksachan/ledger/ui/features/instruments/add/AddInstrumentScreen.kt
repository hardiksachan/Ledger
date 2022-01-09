package com.hardiksachan.ledger.ui.features.instruments.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.hardiksachan.ledger.presentation_logic.instruments.add.AddInstrumentViewModel
import com.hardiksachan.ledger.ui.base.scopednav.navigation.NoParams
import com.hardiksachan.ledger.ui.base.scopednav.navigation.ScreenDestination

object AddInstrumentScreen: ScreenDestination<NoParams>(pathRoot = "addInstrumentScreen")

@Composable
fun AddInstrumentScreen(vm: AddInstrumentViewModel) {
    val name = vm.name.collectAsState()
    val openingBalance = vm.openingBalance.collectAsState()
    val color = vm.color.collectAsState()


    
}