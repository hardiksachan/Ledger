package com.hardiksachan.ledger.data.mappers

import com.hardiksachan.ledger.data.local.Mode
import com.hardiksachan.ledger.domain.model.Mode as ModeDomain

fun Mode.toDomain() = ModeDomain(
    name, color
)

fun List<Mode>.toDomain() = this.map { it.toDomain() }