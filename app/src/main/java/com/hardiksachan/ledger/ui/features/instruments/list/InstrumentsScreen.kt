package com.hardiksachan.ledger.ui.features.instruments.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hardiksachan.ledger.R
import com.hardiksachan.ledger.common.ResultWrapper
import com.hardiksachan.ledger.domain.model.Instrument
import com.hardiksachan.ledger.presentation_logic.instruments.list.InstrumentsViewModel
import com.hardiksachan.ledger.ui.base.scopednav.navigation.NoParams
import com.hardiksachan.ledger.ui.base.scopednav.navigation.ScreenDestination
import com.hardiksachan.ledger.ui.features.widgets.ScreenTitle
import com.hardiksachan.ledger.ui.features.widgets.Table
import com.hardiksachan.ledger.ui.theme.LedgerTheme
import com.hardiksachan.ledger.ui.utils.toRupee

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
                InstrumentsTable(
                    instrumentList = res.result,
                    onUserClick = { viewModel.onInstrumentClicked(it) })
            }
        }
    }
}

@Composable
private fun InstrumentsTable(
    instrumentList: List<Instrument>,
    onUserClick: (Instrument) -> Unit
) {
    val headerCellTitle: @Composable (Int) -> Unit = { index ->
        val value = when (index) {
            0 -> "Instrument"
            1 -> "Opening Balance"
            2 -> "Current Balance"
            else -> ""
        }

        Text(
            text = value,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontWeight = FontWeight.Black,
            textDecoration = TextDecoration.Underline
        )
    }

    val cellWidth: (Int) -> Dp = { index ->
        when (index) {
            0 -> 150.dp
            else -> 190.dp
        }
    }

    val cellText: @Composable (Int, Instrument) -> Unit = { index, item ->
        val value = when (index) {
            0 -> item.name
            1 -> item.openingBalance.toRupee()
            2 -> item.currBalance.toRupee()
            else -> ""
        }

        Text(
            text = value,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }

    Table(
        columnCount = 3,
        cellWidth = cellWidth,
        data = instrumentList,
        modifier = Modifier.verticalScroll(rememberScrollState()),
        headerCellContent = headerCellTitle,
        cellContent = cellText
    )
}

@Preview
@Composable
fun InstrumentTablePreview() {
    LedgerTheme {
        Surface {
            InstrumentsTable(
                instrumentList = listOf(
                    Instrument(
                        id = "001",
                        name = "Wallet",
                        openingBalance = 10_000_00L,
                        currBalance = 8_000_00L,
                        color = 5
                    ),
                    Instrument(
                        id = "002",
                        name = "Paytm",
                        openingBalance = 1_000_00L,
                        currBalance = 3_000_00L,
                        color = 3
                    )
                ),
                onUserClick = {}
            )
        }
    }
}