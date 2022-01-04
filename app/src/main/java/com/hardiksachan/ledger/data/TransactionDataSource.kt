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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.datetime.Instant
import java.util.*

class TransactionDataSource(
    private val categoryQueries: CategoryEntityQueries,
    private val modeQueries: ModeEntityQueries,
    private val ledgerEntryQueries: TransactionEntityQueries,
    private val ledgerCategoryIntermediateQueries: TransactionCategoryIntermediateQueries,
    private val dispatcherProvider: IDispatcherProvider
) : ITransactionRepository {
    // ---------- MODE ----------

    override suspend fun addMode(name: String, color: Int) {
        withContext(dispatcherProvider.IO) {
            modeQueries
                .addMode(
                    UUID.randomUUID().toString(),
                    name,
                    color
                )
        }
    }

    override fun getAllModes(): Flow<ResultWrapper<Exception, List<Mode>>> =
        modeQueries
            .getAllModes()
            .mapModes()

    override fun searchModes(q: String): Flow<ResultWrapper<Exception, List<Mode>>> =
        modeQueries
            .queryModeByName(q)
            .mapModes()

    override suspend fun removeModeIfUnused(id: BackendID) {
        val count = modeQueries.getModeUsedCount(id).executeAsOne()
        if (count == 0L) {
            modeQueries.deleteModeById(id)
        }
        // TODO: what if mode is used?
    }

    // ---------- CATEGORY -------------

    override suspend fun addCategory(name: String, color: Int) {
        withContext(dispatcherProvider.IO) {
            categoryQueries
                .addCategory(
                    UUID.randomUUID().toString(),
                    name,
                    color
                )
        }
    }

    override fun getAllCategories(): Flow<ResultWrapper<Exception, List<Category>>> =
        categoryQueries
            .getAllCategories()
            .mapCategories()

    override fun searchCategories(q: String): Flow<ResultWrapper<Exception, List<Category>>> =
        categoryQueries
            .queryCategoryByName(q)
            .mapCategories()

    override suspend fun removeCategoryIfUnused(id: BackendID) {
        val count = categoryQueries.getCategoryUsedCount(id).executeAsOne()
        if (count == 0L) {
            categoryQueries.deleteCategoryById(id)
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
            ledgerEntryQueries.transaction {
                ledgerEntryQueries
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
                ledgerCategoryIntermediateQueries.transaction {
                    categories.forEach { category ->
                        ledgerCategoryIntermediateQueries
                            .addCategoryToLedgerEntry(
                                id,
                                category.id
                            )
                    }
                }
            }
        }
    }

    override suspend fun modifyTransaction(transaction: Transaction) {

    }

    override fun getTransactions(): Flow<ResultWrapper<Exception, List<Transaction>>> {
        TODO("Not yet implemented")
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