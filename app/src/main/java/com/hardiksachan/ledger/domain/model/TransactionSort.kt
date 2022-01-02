package com.hardiksachan.ledger.domain.model

sealed class TransactionSort(
    desc: Boolean,
    next: TransactionSort?
) {
    data class ByTitle(
        val desc: Boolean = false,
        val next: TransactionSort? = null
    ) : TransactionSort(desc, next)

    data class ByAmount(
        val desc: Boolean = false,
        val next: TransactionSort? = null
    ) : TransactionSort(desc, next)

    data class ByCreationDate(
        val desc: Boolean = false,
        val next: TransactionSort? = null
    ) : TransactionSort(desc, next)

}
