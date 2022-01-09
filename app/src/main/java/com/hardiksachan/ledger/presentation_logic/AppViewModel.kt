package com.hardiksachan.ledger.presentation_logic

import com.hardiksachan.ledger.ui.base.savestate.StateSaver

private const val BottomBarKey = "BottomBar_AppStateSaverKey"

class AppViewModel(
    saver: StateSaver,
    bottomBarSelection: String,
) : BaseViewModel() {
    val bottomBarSelection = saver.getAutoSaveFlow(coroutineScope, BottomBarKey, bottomBarSelection)

    fun onBottomDestinationChanged(destination: String) {
        bottomBarSelection.value = destination
    }
}