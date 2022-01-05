package com.hardiksachan.ledger.ui.features.instruments.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hardiksachan.ledger.R
import com.hardiksachan.ledger.common.ResultWrapper
import com.hardiksachan.ledger.domain.model.Instrument
import com.hardiksachan.ledger.presentation_logic.instruments.list.InstrumentsViewModel
import com.hardiksachan.ledger.ui.base.scopednav.navigation.NoParams
import com.hardiksachan.ledger.ui.base.scopednav.navigation.ScreenDestination
import com.hardiksachan.ledger.ui.features.widgets.ScreenTitle

object InstrumentsScreen : ScreenDestination<NoParams>(pathRoot = "instrumentsScreen")

@Composable
fun InstrumentsScreen(
    viewModel: InstrumentsViewModel
) {
    val instruments = viewModel.instruments
        .collectAsState(initial = ResultWrapper.Success(emptyList()))

    Column(Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp)) {
        ScreenTitle(text = stringResource(R.string.instruments_screen_title))

        when (val res = instruments.value) {
            is ResultWrapper.Failure -> Text(text = "error:\n${res.error}")
            is ResultWrapper.Success -> {
                Text(res.toString())
//                InstrumentsList(
//                    instrumentList = res.result,
//                    onUserClick = { viewModel.onInstrumentClicked(it) }
//                )
            }
        }
    }
}

@Composable
private fun InstrumentsList(instrumentList: List<Instrument>, onUserClick: (Instrument) -> Unit) {
    if (instrumentList.isNotEmpty()) {
        LazyColumn {
            item { Spacer(Modifier.height(8.dp)) }
            items(instrumentList) { instrument ->
                InstrumentRow(instrument = instrument, onCityClick = onUserClick)
            }
            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

@Composable
fun InstrumentRow(instrument: Instrument, onCityClick: (Instrument) -> Unit) {
    Text(instrument.toString())
}
