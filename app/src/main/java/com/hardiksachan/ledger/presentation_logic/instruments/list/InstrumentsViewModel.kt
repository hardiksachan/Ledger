package com.hardiksachan.ledger.presentation_logic.instruments.list

import com.hardiksachan.ledger.domain.repository.IInstrumentRepository
import com.hardiksachan.ledger.presentation_logic.BaseViewModel

class InstrumentsViewModel(
    instrumentsRepo: IInstrumentRepository,
    private val onAdd: () -> Unit
) : BaseViewModel() {
    val instruments = instrumentsRepo.getAllInstruments()

    fun onEvent(event: InstrumentsListEvents) {
        when (event) {
            InstrumentsListEvents.OnAddInstrumentPressed -> onAdd()
        }
    }
}