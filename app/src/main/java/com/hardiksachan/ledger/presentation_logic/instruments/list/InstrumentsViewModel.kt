package com.hardiksachan.ledger.presentation_logic.instruments.list

import com.hardiksachan.ledger.common.BackendID
import com.hardiksachan.ledger.domain.repository.IInstrumentRepository

class InstrumentsViewModel(
    private val instrumentsRepo: IInstrumentRepository,
    private val navToInstrument: (BackendID) -> Unit,
) {
}