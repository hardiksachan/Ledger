package com.hardiksachan.ledger.data

import com.hardiksachan.ledger.common.BackendID
import com.hardiksachan.ledger.common.IDispatcherProvider
import com.hardiksachan.ledger.common.ResultWrapper
import com.hardiksachan.ledger.data.local.InsrumentEntityQueries
import com.hardiksachan.ledger.data.local.InstrumentExp
import com.hardiksachan.ledger.data.local.InstrumentExpandedQueries
import com.hardiksachan.ledger.data.mappers.toDomain
import com.hardiksachan.ledger.domain.model.Instrument
import com.hardiksachan.ledger.domain.repository.IInstrumentRepository
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOne
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*

class InstrumentDataSource(
    private val baseQueries: InsrumentEntityQueries,
    private val expandedQueries: InstrumentExpandedQueries,
    private val dispatcherProvider: IDispatcherProvider
) : IInstrumentRepository {
    override suspend fun addInstrument(name: String, openingBalance: Long, color: Int) {
        withContext(dispatcherProvider.IO) {
            baseQueries
                .addInstrument(
                    UUID.randomUUID().toString(),
                    name,
                    openingBalance,
                    color
                )
        }
    }

    override fun getAllInstruments(): Flow<ResultWrapper<Exception, List<Instrument>>> =
        expandedQueries
            .getAllInstruments()
            .mapQueryToResultWrapperFlow()

    override fun searchInstruments(q: String): Flow<ResultWrapper<Exception, List<Instrument>>> =
        expandedQueries
            .searchInstruments(q)
            .mapQueryToResultWrapperFlow()

    override fun getInstrument(id: BackendID): Flow<ResultWrapper<Exception, Instrument>> =
        expandedQueries
            .getInstrument(id)
            .asFlow()
            .mapToOne(dispatcherProvider.IO)
            .map {
                ResultWrapper.build {
                    it.toDomain()
                }
            }
            .catch { e ->
                ResultWrapper.Failure(e)
            }
            .flowOn(dispatcherProvider.IO)

    private fun Query<InstrumentExp>.mapQueryToResultWrapperFlow() =
        this
            .asFlow()
            .mapToList(dispatcherProvider.IO)
            .map { expandedInstruments ->
                ResultWrapper.build {
                    expandedInstruments.toDomain()
                }
            }
            .catch { e ->
                ResultWrapper.Failure(e)
            }
            .flowOn(dispatcherProvider.IO)

}
