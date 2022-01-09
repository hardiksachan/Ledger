package com.hardiksachan.ledger.presentation_logic.instruments.add

sealed class AddInstrumentEvent {
    data class OnNameChanged(val name: String): AddInstrumentEvent()
    data class OnOpeningBalanceChanged(val amount: Long): AddInstrumentEvent()
    data class OnColorChanged(val color: Int): AddInstrumentEvent()
    object OnSavePressed: AddInstrumentEvent()
    object OnCancelPressed: AddInstrumentEvent()
}
