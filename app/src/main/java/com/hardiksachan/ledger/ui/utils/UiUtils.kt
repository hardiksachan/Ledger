package com.hardiksachan.ledger.ui.utils

fun Long.toRupee(): String = this.toString().padStart(length = 3, padChar = '0').let { base ->
    "${base.dropLast(2)}.${base.takeLast(2)}"
}
