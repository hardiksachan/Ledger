package com.hardiksachan.ledger.presentation_logic.instruments.list

import com.hardiksachan.ledger.common.BackendID
import com.hardiksachan.ledger.domain.repository.IInstrumentRepository
import com.hardiksachan.ledger.presentation_logic.BaseViewModel
import kotlinx.coroutines.launch

class InstrumentsViewModel(
    private val instrumentsRepo: IInstrumentRepository,
    private val onAdd: () -> Unit
) : BaseViewModel() {
    val instruments = instrumentsRepo.getAllInstruments()

    fun onEvent(event: InstrumentsListEvents) {
        when (event) {
            InstrumentsListEvents.OnAddInstrumentPressed -> onAdd()
            is InstrumentsListEvents.OnDeletePressed -> deleteInstrument(event.id)
        }
    }

    private fun deleteInstrument(id: BackendID) {
        try {
            coroutineScope.launch {
                instrumentsRepo.deleteInstrumentAssumingUnused(id)
            }
        } catch (e: Exception) {
            // TODO: show snackbar
        }
    }
}