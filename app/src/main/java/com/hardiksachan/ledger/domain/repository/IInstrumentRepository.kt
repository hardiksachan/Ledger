package com.hardiksachan.ledger.domain.repository

import com.hardiksachan.ledger.common.ResultWrapper
import com.hardiksachan.ledger.common.BackendID
import com.hardiksachan.ledger.domain.model.Instrument
import kotlinx.coroutines.flow.Flow

interface IInstrumentRepository {

    suspend fun addInstrument(
        name: String,
        openingBalance: Long,
        color: Int
    )

    fun getAllInstruments(): Flow<ResultWrapper<Exception, List<Instrument>>>

    fun searchInstruments(q: String): Flow<ResultWrapper<Exception, List<Instrument>>>

    fun getInstrument(id: BackendID): Flow<ResultWrapper<Exception, Instrument>>
}