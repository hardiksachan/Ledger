package com.hardiksachan.ledger.domain.model

import kotlinx.datetime.Instant

data class TransactionFilter(
    val title: String? = null,
    val amount: LongRange? = null,
    val isDebit: Boolean? = null,
    val instrument: List<Instrument>? = null,
    val categories: List<Category>? = null,
    val mode: List<Mode>? = null,
    val remark: String? = null,
    val createdAtStart: Instant? = null,
    val createdAtEnd: Instant? = null,
)