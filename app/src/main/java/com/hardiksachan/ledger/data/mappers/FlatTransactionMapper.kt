package com.hardiksachan.ledger.data.mappers

import com.hardiksachan.ledger.data.local.FlatTransaction
import com.hardiksachan.ledger.domain.model.Category
import com.hardiksachan.ledger.domain.model.Instrument
import com.hardiksachan.ledger.domain.model.Mode
import com.hardiksachan.ledger.domain.model.Transaction
import kotlinx.datetime.Instant

fun FlatTransaction.toDomain() = Transaction(
    id,
    title,
    amount,
    isDebit,
    Instrument(
        instrumentId,
        instrumentName!!,
        openingBalance!!,
        currBalance?.toLong() ?: 0L,
        instrumentColor!!
    ),
    categoryJson.split(";").dropLast(1).map { single ->
        val props = single.split("/?/")
        Category(
            props[0],
            props[1].toInt()
        )
    },
    Mode(
        modeName,
        modeColor!!
    ),
    remark,
    Instant.parse(createdAt)
)

fun List<FlatTransaction>.toDomain() = this.map { it.toDomain() }