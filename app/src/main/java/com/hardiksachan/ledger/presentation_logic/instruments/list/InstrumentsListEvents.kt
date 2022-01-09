package com.hardiksachan.ledger.presentation_logic.instruments.list

import com.hardiksachan.ledger.common.BackendID

sealed class InstrumentsListEvents {
    data class OnDeletePressed(val id: BackendID): InstrumentsListEvents()
    object OnAddInstrumentPressed: InstrumentsListEvents()
}
