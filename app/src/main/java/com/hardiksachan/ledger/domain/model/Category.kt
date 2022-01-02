package com.hardiksachan.ledger.domain.model

import com.hardiksachan.ledger.common.BackendID

data class Category(
    val id: BackendID,
    val name: String,
    val color: Int
)
