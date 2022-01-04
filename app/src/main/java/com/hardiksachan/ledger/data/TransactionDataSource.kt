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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import java.util.*

class TransactionDataSource(
    private val transactionQueries: TransactionContextQueries,
    private val instrumentExpandedQueries: InstrumentExpandedQueries,
    private val dispatcherProvider: IDispatcherProvider,
    private val applicationScope: CoroutineScope
) : ITransactionRepository {
    fun modesFlow() = transactionQueries
        .getAllModes()
        .asFlow()
        .mapToList(dispatcherProvider.IO)
        .flowOn(dispatcherProvider.IO)
        .shareIn(applicationScope, SharingStarted.Lazily)

    fun categoriesFlow() = transactionQueries
        .getAllCategories()
        .asFlow()
        .mapToList(dispatcherProvider.IO)
        .flowOn(dispatcherProvider.IO)
        .shareIn(applicationScope, SharingStarted.Lazily)

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
        val count = transactionQueries.getModeUsedCount(id).executeAsOne()
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
                    UUID.randomUUID().toString(),
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

    override suspend fun removeCategoryIfUnused(id: BackendID) {
        val count = transactionQueries.getCategoryUsedCount(id).executeAsOne()
        if (count == 0L) {
            transactionQueries.deleteCategoryById(id)
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
                        mode.id,
                        remark,
                        createdAt.toString()
                    )
                categories.forEach { category ->
                    transactionQueries
                        .addCategoryToLedgerEntry(
                            id,
                            category.id
                        )
                }

            }
        }
    }

    override suspend fun modifyTransaction(transaction: Transaction) {
        transactionQueries.transaction {
            transactionQueries.removeAllCategoriesFromLedgerEntry(transaction.id)
            transactionQueries.deleteLedgerEntry(transaction.id)
            with (transaction) {
                transactionQueries.insertLedgerEntry(
                    id,
                    title,
                    amount,
                    isDebit,
                    instrument.id,
                    mode.id,
                    remark,
                    createdAt.toString()
                )
            }
            transaction.categories.forEach { category ->
                transactionQueries
                    .addCategoryToLedgerEntry(
                        transaction.id,
                        category.id
                    )
            }
        }
    }

    override fun getTransactions(): Flow<ResultWrapper<Exception, List<Transaction>>> {
        val ledgerEntryFlow = transactionQueries
            .getAllLedgerEntries()
            .asFlow()
            .mapToList()

        val modesFlow = modesFlow()

        val categoriesFlow = categoriesFlow()


        combine(ledgerEntryFlow, modesFlow, categoriesFlow) { entries, modes, categories ->
            entries.map { entry ->
                Transaction(
                    entry.id,
                    entry.title,
                    entry.amount,
                    entry.isDebit,
                    entry.ins
                )
            }
        }
    }
    override fun getTransactionById(id: BackendID): Flow<ResultWrapper<Exception, Transaction>> {
        TODO("Not yet implemented")
    }

    override fun deleteTransaction(id: BackendID) {
        TODO("Not yet implemented")
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