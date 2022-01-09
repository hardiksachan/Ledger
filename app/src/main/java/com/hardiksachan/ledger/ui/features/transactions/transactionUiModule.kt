package com.hardiksachan.ledger.ui.features.transactions

import com.hardiksachan.ledger.presentation_logic.Clearable
import com.hardiksachan.ledger.presentation_logic.transactions.list.TransactionListViewModel
import com.hardiksachan.ledger.ui.base.scopednav.navigation.navGraphScope
import com.hardiksachan.ledger.ui.features.transactions.list.TransactionListScreen
import org.koin.dsl.bind
import org.koin.dsl.module

val transactionUiModule = module {
    navGraphScope<TransactionsSubgraph> {
    }

    scope<TransactionListScreen> {
        scoped { (navToAdd: () -> Unit) ->
            TransactionListViewModel(get(), navToAdd)
        } bind Clearable::class
    }
}