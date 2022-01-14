package com.hardiksachan.ledger.presentation_logic.transactions.add

import com.hardiksachan.ledger.domain.repository.IInstrumentRepository
import com.hardiksachan.ledger.domain.repository.ITransactionRepository
import com.hardiksachan.ledger.presentation_logic.BaseViewModel
import com.hardiksachan.ledger.ui.base.savestate.StateSaver
import kotlinx.datetime.Clock

private const val TitleKey = "AddTransactionTitleStateSaverKey"
private const val AmountKey = "AddTransactionAmountStateSaverKey"
private const val IsDebitKey = "AddTransactionIsDebitStateSaverKey"
private const val InstrumentIdKey = "AddTransactionInstrumentIdStateSaverKey"
private const val CategoriesKey = "AddTransactionCategoriesStateSaverKey"
private const val ModeKey = "AddTransactionModeStateSaverKey"
private const val RemarkKey = "AddTransactionRemarkStateSaverKey"

class AddTransactionViewModel(
    saver: StateSaver,
    private val transactionRepo: ITransactionRepository,
    private val instrumentsRepo: IInstrumentRepository,
    private val clock: Clock,
    isDebit: Boolean,
    private val onDone: () -> Unit
): BaseViewModel() {

    private val _title = saver.getAutoSaveFlow(coroutineScope, TitleKey, "")
    private val _amount = saver.getAutoSaveFlow(coroutineScope, AmountKey, 0L)
    private val _isDebit = saver.getAutoSaveFlow(coroutineScope, IsDebitKey, isDebit)
    private val _instrumentId = saver.getAutoSaveFlow(coroutineScope, InstrumentIdKey, "")
    private val _categoryIds = saver.getAutoSaveFlow(coroutineScope, CategoriesKey, "")
    private val _modeId = saver.getAutoSaveFlow(coroutineScope, ModeKey, "")
    private val _remark = saver.getAutoSaveFlow(coroutineScope, RemarkKey, "")

    private fun onSave() {
        // TODO
        onDone ()
    }
}