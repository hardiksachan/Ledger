package com.hardiksachan.ledger.domain

import kotlinx.datetime.Instant

data class Transaction(
    val title: String,
    val amount: Long,
    val isDebit: Boolean,
    val instrument: Instrument,
    val categories: List<Category>,
    val mode: Mode,
    val remark: String,
    val createdAt: Instant
)
