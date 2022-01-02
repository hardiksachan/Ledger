package com.hardiksachan.ledger.domain

data class Instrument(
    val name: String,
    val openingBalance: Long,
    val currBalance: Long,
    val color: Int,
)
