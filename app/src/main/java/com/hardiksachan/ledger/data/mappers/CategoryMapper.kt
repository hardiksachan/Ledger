package com.hardiksachan.ledger.data.mappers

import com.hardiksachan.ledger.data.local.Category
import com.hardiksachan.ledger.domain.model.Category as CategoryDomain

fun Category.toDomain() = CategoryDomain(
    id, name, color
)

fun List<Category>.toDomain() = this.map { it.toDomain() }