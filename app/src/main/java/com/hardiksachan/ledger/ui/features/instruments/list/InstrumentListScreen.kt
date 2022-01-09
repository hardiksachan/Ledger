package com.hardiksachan.ledger.ui.features.instruments.list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hardiksachan.ledger.R
import com.hardiksachan.ledger.common.ResultWrapper
import com.hardiksachan.ledger.domain.model.Instrument
import com.hardiksachan.ledger.presentation_logic.instruments.list.InstrumentsListEvents
import com.hardiksachan.ledger.presentation_logic.instruments.list.InstrumentsViewModel
import com.hardiksachan.ledger.ui.base.scopednav.navigation.NoParams
import com.hardiksachan.ledger.ui.base.scopednav.navigation.ScreenDestination
import com.hardiksachan.ledger.ui.features.widgets.ScreenTitle
import com.hardiksachan.ledger.ui.theme.LedgerTheme
import com.hardiksachan.ledger.ui.utils.toColor
import com.hardiksachan.ledger.ui.utils.toRupee

object InstrumentListScreen : ScreenDestination<NoParams>(pathRoot = "instrumentListScreen")

@Composable
fun InstrumentListScreen(
    viewModel: InstrumentsViewModel,
    bottomBar: @Composable () -> Unit
) {
    val instruments = viewModel.instruments
        .collectAsState(initial = ResultWrapper.Success(emptyList()))

    Scaffold(
        Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(InstrumentsListEvents.OnAddInstrumentPressed)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(Icons.Filled.Add, "Add instrument")
            }
        },
        topBar = {
            ScreenTitle(
                text = stringResource(R.string.instruments_screen_title),
                modifier = Modifier.padding(16.dp)
            )
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        Box(modifier = Modifier
            .padding(innerPadding)
            .padding(horizontal = 16.dp)) {
            when (val res = instruments.value) {
                is ResultWrapper.Failure -> Text(text = "error:\n${res.error}")
                is ResultWrapper.Success -> {
                    InstrumentsList(
                        instrumentList = res.result,
                        onUserClick = { })
                }
            }
        }
    }
}

@Composable
private fun InstrumentsList(
    instrumentList: List<Instrument>,
    onUserClick: (Instrument) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(instrumentList) { instrument ->
            InstrumentListItem(
                instrument = instrument,
                modifier = Modifier
                    .fillMaxWidth()
            ) {}
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun InstrumentListItem(
    instrument: Instrument,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    onUserClick: (Instrument) -> Unit
) {
    Box(
        modifier = modifier
    ) {
        Text(instrument.name)
        Canvas(modifier = Modifier.matchParentSize()) {
            drawRoundRect(
                color = instrument.color.toColor(),
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )

        }
        Column(
            modifier = Modifier
                .padding(16.dp)
                .padding(end = 32.dp)
        ) {
            Text(
                text = instrument.name,
                style = MaterialTheme.typography.h6,
                color = MaterialTheme.colors.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = instrument.currBalance.toRupee(),
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onSurface,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(
            onClick = { },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete note",
                tint = MaterialTheme.colors.onSurface
            )
        }
    }
}

@Preview
@Composable
fun InstrumentTablePreview() {
    LedgerTheme {
        InstrumentListItem(
            instrument = Instrument(
                id = "001",
                name = "Wallet",
                openingBalance = 10_000_00L,
                currBalance = 8_000_00L,
                color = 1
            ),
            modifier = Modifier.fillMaxWidth(),
            onUserClick = {}
        )
    }
}