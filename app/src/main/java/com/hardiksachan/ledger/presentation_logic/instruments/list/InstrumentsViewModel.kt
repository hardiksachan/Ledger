package com.hardiksachan.ledger.presentation_logic.instruments.list

import com.hardiksachan.ledger.common.BackendID
import com.hardiksachan.ledger.domain.model.Instrument
import com.hardiksachan.ledger.domain.repository.IInstrumentRepository
import com.hardiksachan.ledger.presentation_logic.BaseViewModel

class InstrumentsViewModel(
    private val instrumentsRepo: IInstrumentRepository,
    private val navToInstrument: (BackendID) -> Unit,
) : BaseViewModel() {
    val instruments = instrumentsRepo.getAllInstruments()

    fun onInstrumentClicked(instrument: Instrument) {
        navToInstrument(instrument.id)
    }
}