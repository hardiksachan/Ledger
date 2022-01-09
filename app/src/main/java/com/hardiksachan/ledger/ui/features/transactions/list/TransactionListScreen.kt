package com.hardiksachan.ledger.ui.features.transactions.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hardiksachan.ledger.R
import com.hardiksachan.ledger.common.ResultWrapper
import com.hardiksachan.ledger.presentation_logic.transactions.list.TransactionListEvent
import com.hardiksachan.ledger.presentation_logic.transactions.list.TransactionListViewModel
import com.hardiksachan.ledger.ui.base.scopednav.navigation.NoParams
import com.hardiksachan.ledger.ui.base.scopednav.navigation.ScreenDestination
import com.hardiksachan.ledger.ui.features.widgets.ScreenTitle

object TransactionListScreen: ScreenDestination<NoParams>(pathRoot = "transactionListScreen")

@Composable
fun TransactionListScreen(
    viewModel: TransactionListViewModel,
    bottomBar: @Composable () -> Unit
) {
    val transactions = viewModel.transactions
        .collectAsState(initial = ResultWrapper.Success(emptyList()))

    Scaffold(
        Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(TransactionListEvent.OnAddTransactionPressed)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
                Icon(Icons.Filled.Add, "Add Transaction")
            }
        },
        topBar = {
            ScreenTitle(
                text = stringResource(R.string.transactions_screen_title),
                modifier = Modifier.padding(16.dp)
            )
        },
        bottomBar = bottomBar
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            when (val res = transactions.value) {
                is ResultWrapper.Failure -> Text(text = "error:\n${res.error}")
                is ResultWrapper.Success -> {
                    Text(res.result.toString())
                }
            }
        }
    }
}