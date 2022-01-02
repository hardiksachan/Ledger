package com.hardiksachan.ledger.domain.repository

import com.hardiksachan.ledger.common.ResultWrapper
import com.hardiksachan.ledger.common.BackendID
import com.hardiksachan.ledger.domain.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

interface ITransactionRepository {

    // Mode
    suspend fun addMode(
        name: String,
        color: Int
    )

    fun getAllModes(): Flow<ResultWrapper<Exception, List<Mode>>>

    fun searchModes(q: String): Flow<ResultWrapper<Exception, List<Mode>>>

    suspend fun removeModeIfUnused(id: BackendID)

    // Category
    suspend fun addCategory(
        name: String,
        color: Int
    )

    fun getAllCategories(): Flow<ResultWrapper<Exception, List<Category>>>

    fun searchCategories(q: String): Flow<ResultWrapper<Exception, List<Category>>>

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

    fun getTransactions(): Flow<ResultWrapper<Exception, List<Transaction>>>

    fun getTransactionById(id: BackendID): Flow<ResultWrapper<Exception, Transaction>>

    fun queryTransactions(
        filter: TransactionFilter,
        sort: TransactionSort
    ): Flow<ResultWrapper<Exception, List<Transaction>>>

    fun deleteTransaction(id: BackendID)

}