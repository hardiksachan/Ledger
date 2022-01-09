package com.hardiksachan.ledger.presentation_logic.transactions.list

import com.hardiksachan.ledger.domain.repository.ITransactionRepository
import com.hardiksachan.ledger.presentation_logic.BaseViewModel

class TransactionListViewModel(
    private val transactionsRepo: ITransactionRepository,
    private val onAdd: () -> Unit
) : BaseViewModel() {
    val transactions = transactionsRepo.getTransactions()

    fun onEvent(event: TransactionListEvent) {
        when (event) {
            TransactionListEvent.OnAddTransactionPressed -> onAdd()
        }
    }
}