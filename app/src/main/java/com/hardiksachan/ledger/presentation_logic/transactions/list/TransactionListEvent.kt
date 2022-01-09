package com.hardiksachan.ledger.presentation_logic.transactions.list

sealed class TransactionListEvent {
    object OnAddTransactionPressed: TransactionListEvent()
}
