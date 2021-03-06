package com.hardiksachan.ledger.domain.model

import com.hardiksachan.ledger.common.BackendID

data class Instrument(
    val id: BackendID,
    val name: String,
    val openingBalance: Long,
    val currBalance: Long,
    val color: Int,
)
