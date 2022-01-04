package com.hardiksachan.ledger.data

import com.hardiksachan.ledger.common.BackendID
import com.hardiksachan.ledger.common.IDispatcherProvider
import com.hardiksachan.ledger.common.ResultWrapper
import com.hardiksachan.ledger.data.local.*
import com.hardiksachan.ledger.data.local.Mode as DbMode
import com.hardiksachan.ledger.data.local.Category as DbCategory
import com.hardiksachan.ledger.data.mappers.toDomain
import com.hardiksachan.ledger.domain.model.Category
import com.hardiksachan.ledger.domain.model.Instrument
import com.hardiksachan.ledger.domain.model.Mode
import com.hardiksachan.ledger.domain.model.Transaction
import com.hardiksachan.ledger.domain.repository.ITransactionRepository
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import java.util.*

class TransactionDataSource(
    private val transactionQueries: TransactionContextQueries,
    private val dispatcherProvider: IDispatcherProvider
) : ITransactionRepository {
    // ---------- MODE ----------
    override suspend fun addMode(name: String, color: Int) {
        withContext(dispatcherProvider.IO) {
            transactionQueries
                .addMode(
                    name,
                    color
                )
        }
    }

    override fun getAllModes(): Flow<ResultWrapper<Exception, List<Mode>>> =
        transactionQueries
            .getAllModes()
            .mapModes()

    override fun searchModes(q: String): Flow<ResultWrapper<Exception, List<Mode>>> =
        transactionQueries
            .queryModeByName(q)
            .mapModes()

    override suspend fun removeModeIfUnused(name: String) {
        val count = transactionQueries.getModeUsedCount(name).executeAsOne()
        if (count == 0L) {
            transactionQueries.deleteModeByName(name)
        }
        // TODO: what if mode is used?
    }

    // ---------- CATEGORY -------------
    override suspend fun addCategory(name: String, color: Int) {
        withContext(dispatcherProvider.IO) {
            transactionQueries
                .addCategory(
                    name,
                    color
                )
        }
    }

    override fun getAllCategories(): Flow<ResultWrapper<Exception, List<Category>>> =
        transactionQueries
            .getAllCategories()
            .mapCategories()

    override fun searchCategories(q: String): Flow<ResultWrapper<Exception, List<Category>>> =
        transactionQueries
            .queryCategoryByName(q)
            .mapCategories()

    override suspend fun removeCategoryIfUnused(name: String) {
        val count = transactionQueries.getCategoryUsedCount(name).executeAsOne()
        if (count == 0L) {
            transactionQueries.deleteCategoryByName(name)
        }
        // TODO: what if category is used?
    }

    // -------- TRANSACTIONS -------

    override suspend fun insertTransaction(
        title: String,
        amount: Long,
        isDebit: Boolean,
        instrument: Instrument,
        categories: List<Category>,
        mode: Mode,
        remark: String,
        createdAt: Instant
    ) {
        withContext(dispatcherProvider.IO) {
            val id = UUID.randomUUID().toString()
            transactionQueries.transaction {
                transactionQueries
                    .insertLedgerEntry(
                        id,
                        title,
                        amount,
                        isDebit,
                        instrument.id,
                        mode.name,
                        remark,
                        createdAt.toString()
                    )
                categories.forEach { category ->
                    transactionQueries
                        .addCategoryToLedgerEntry(
                            id,
                            category.name
                        )
                }

            }
        }
    }

    override suspend fun modifyTransaction(transaction: Transaction) {
        transactionQueries.transaction {
            with (transaction) {
                transactionQueries.insertLedgerEntry(
                    id,
                    title,
                    amount,
                    isDebit,
                    instrument.id,
                    mode.name,
                    remark,
                    createdAt.toString()
                )
            }
            transaction.categories.forEach { category ->
                transactionQueries
                    .addCategoryToLedgerEntry(
                        transaction.id,
                        category.name
                    )
            }
        }
    }

    override fun getTransactions(): Flow<ResultWrapper<Exception, List<Transaction>>> =
        transactionQueries
            .getAllFlatTransactions()
            .mapFlatTransactions()

    override fun getTransactionById(id: BackendID): Flow<ResultWrapper<Exception, Transaction>> =
        transactionQueries
            .getFlatTransactionById(id)
            .asFlow()
            .mapToOne(dispatcherProvider.IO)
            .map {
                ResultWrapper.build { it.toDomain() }
            }
            .catch { e ->
                ResultWrapper.Failure(e)
            }
            .flowOn(dispatcherProvider.IO)

    override fun deleteTransaction(id: BackendID) {
        transactionQueries.removeAllCategoriesFromLedgerEntry(id)
        transactionQueries.deleteLedgerEntry(id)
    }

    // --------- HELPERS ------------

    private fun Query<DbMode>.mapModes() =
        mapQuery { modes ->
            modes.toDomain()
        }

    private fun Query<DbCategory>.mapCategories() =
        mapQuery { categories ->
            categories.toDomain()
        }

    private fun Query<FlatTransaction>.mapFlatTransactions() =
        mapQuery { flatTransactions ->
            flatTransactions.toDomain()
        }

    private fun <T : Any, R> Query<T>.mapQuery(
        domainMapper: (List<T>) -> R
    ) =
        this
            .asFlow()
            .mapToList(dispatcherProvider.IO)
            .map {
                ResultWrapper.build { domainMapper(it) }
            }
            .catch { e ->
                ResultWrapper.Failure(e)
            }
            .flowOn(dispatcherProvider.IO)
}