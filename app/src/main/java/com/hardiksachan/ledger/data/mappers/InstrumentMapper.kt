package com.hardiksachan.ledger.data.mappers

import com.hardiksachan.ledger.data.local.InstrumentExp
import com.hardiksachan.ledger.domain.model.Instrument

fun InstrumentExp.toDomain() = Instrument(
    id, name, openingBalance, currBalance?.toLong() ?: 0L, color
)

fun List<InstrumentExp>.toDomain() = this.map { it.toDomain() }