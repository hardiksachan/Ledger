package com.hardiksachan.ledger.domain.model

import com.hardiksachan.ledger.common.BackendID
import kotlinx.datetime.Instant

data class Transaction(
    val id: BackendID,
    val title: String,
    val amount: Long,
    val isDebit: Boolean,
    val instrument: Instrument,
    val categories: List<Category>,
    val mode: Mode,
    val remark: String,
    val createdAt: Instant
)
