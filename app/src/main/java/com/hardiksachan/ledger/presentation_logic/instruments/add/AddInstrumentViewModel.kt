package com.hardiksachan.ledger.presentation_logic.instruments.add

import com.hardiksachan.ledger.domain.repository.IInstrumentRepository
import com.hardiksachan.ledger.presentation_logic.BaseViewModel
import com.hardiksachan.ledger.ui.base.savestate.StateSaver
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val NameKey = "AddInstrumentNameStateSaverKey"
private const val OpeningBalanceKey = "AddInstrumentOpeningBalanceStateSaverKey"
private const val ColorKey = "AddInstrumentColorStateSaverKey"

class AddInstrumentViewModel(
    saver: StateSaver,
    private val instrumentsRepo: IInstrumentRepository,
    private val onDone: () -> Unit
) : BaseViewModel() {

    private val _name = saver.getAutoSaveFlow(coroutineScope, NameKey, "")
    private val _openingBalance = saver.getAutoSaveFlow(coroutineScope, OpeningBalanceKey, 0L)
    private val _color = saver.getAutoSaveFlow(coroutineScope, ColorKey, 1)

    val name = _name.asStateFlow()
    val openingBalance = _openingBalance.asStateFlow()
    val color = _color.asStateFlow()

    fun onEvent(event: AddInstrumentEvent) {
        when (event) {
            AddInstrumentEvent.OnCancelPressed -> onDone()
            is AddInstrumentEvent.OnColorChanged -> _color.value = event.color
            is AddInstrumentEvent.OnNameChanged -> _name.value = event.name
            is AddInstrumentEvent.OnOpeningBalanceChanged -> _openingBalance.value = event.amount
            AddInstrumentEvent.OnSavePressed -> onSave()
        }
    }


    private fun onSave() {
        coroutineScope.launch {
            instrumentsRepo.addInstrument(
                _name.value,
                _openingBalance.value,
                _color.value,
            )
        }
        onDone()
    }
}