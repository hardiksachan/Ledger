package com.hardiksachan.ledger.ui.features.transactions

import com.hardiksachan.ledger.ui.base.scopednav.navigation.NoParams
import com.hardiksachan.ledger.ui.base.scopednav.navigation.SubgraphDestination
import com.hardiksachan.ledger.ui.features.transactions.list.TransactionListScreen

object TransactionsSubgraph : SubgraphDestination<NoParams, NoParams>(
    pathRoot = "transactionsSubgraph",
    startDestination = TransactionListScreen
)