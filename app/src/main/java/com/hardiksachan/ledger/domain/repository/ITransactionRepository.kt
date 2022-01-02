package com.hardiksachan.ledger.domain.repository

import com.hardiksachan.ledger.domain.BackendID
import com.hardiksachan.ledger.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface ITransactionRepository {

    // Mode
    suspend fun addMode(
        name: String,
        color: Int
    )

    fun getAllModes(): Flow<List<Mode>>

    fun searchModes(q: String): Flow<List<Mode>>

    suspend fun removeModeIfUnused(id: BackendID)

    // Category
    suspend fun addCategory(
        name: String,
        color: Int
    )

    fun getAllCategories(): Flow<List<Category>>

    fun searchCategories(q: String): Flow<List<Category>>

    suspend fun removeCategoryIfUnused(id: BackendID)

    // Transaction
    suspend fun insertTransaction(
        title: String,
        amount: Long,
        isDebit: Boolean,
        instrument: Instrument,
        categories: List<Category>,
        mode: Mode,
        remark: String,
        createdAt: Instant
    )

    suspend fun modifyTransaction(transaction: Transaction)

    fun getTransactions(): Flow<List<Transaction>>

    fun getTransactionById(id: BackendID): Flow<List<Transaction>>

    fun queryTransactions(filter: TransactionFilter, sort: TransactionSort): Flow<List<Transaction>>

    fun deleteTransaction(id: BackendID)

}