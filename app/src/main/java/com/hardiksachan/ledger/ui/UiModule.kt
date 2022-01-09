package com.hardiksachan.ledger.ui

import com.hardiksachan.ledger.presentation_logic.AppViewModel
import com.hardiksachan.ledger.ui.base.savestate.BundleStateSaver
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

val uiModule = module {
    scope<MainActivity> {
    }
    // TODO: not sure if I should put this here
    single { (saver: BundleStateSaver) ->
        AppViewModel(saver, BottomNavItem.Instruments.pathRoot)
    }
}

val getActivityScope = { getKoin().getOrCreateScope<MainActivity>("MainActivity") }