package com.hardiksachan.ledger.ui.utils

import com.hardiksachan.ledger.ui.theme.*

fun Long.toRupee(): String = this.toString().padStart(length = 3, padChar = '0').let { base ->
    "${base.dropLast(2)}.${base.takeLast(2)}"
}

fun Int.toColor() = when (this) {
    1 -> BabyBlue
    2 -> RedOrange
    3 -> RedPink
    4 -> Violet
    else -> LightGreen
}