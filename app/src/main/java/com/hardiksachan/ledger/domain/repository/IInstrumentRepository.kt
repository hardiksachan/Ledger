package com.hardiksachan.ledger.domain.repository

import com.hardiksachan.ledger.domain.BackendID
import com.hardiksachan.ledger.domain.model.Instrument
import kotlinx.coroutines.flow.Flow

interface IInstrumentRepository {

    suspend fun addInstrument(
        name: String,
        openingBalance: Long,
        color: Int
    )

    fun getAllInstruments(): Flow<List<Instrument>>

    fun searchInstruments(q: String): Flow<List<Instrument>>

    fun getInstrument(id: BackendID): Flow<Instrument>
}