package com.hardiksachan.ledger.domain.model

import kotlinx.datetime.Instant

data class TransactionFilter(
    val title: String?,
    val amount: LongRange?,
    val isDebit: Boolean?,
    val instrument: List<Instrument>?,
    val categories: List<Category>?,
    val mode: List<Mode>?,
    val remark: String?,
    val createdAtStart: Instant?,
    val createdAtEnd: Instant?,
)
