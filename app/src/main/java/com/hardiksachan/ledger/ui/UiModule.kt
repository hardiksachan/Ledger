package com.hardiksachan.ledger.ui

import com.hardiksachan.ledger.common.BackendID
import com.hardiksachan.ledger.presentation_logic.AppViewModel
import com.hardiksachan.ledger.presentation_logic.Clearable
import com.hardiksachan.ledger.presentation_logic.instruments.list.InstrumentsViewModel
import com.hardiksachan.ledger.ui.base.savestate.BundleStateSaver
import com.hardiksachan.ledger.ui.features.instruments.list.InstrumentsScreen
import org.koin.dsl.bind
import org.koin.dsl.module
import org.koin.java.KoinJavaComponent.getKoin

val uiModule = module {
    scope<InstrumentsScreen> {
        scoped { (navToInstrument: (BackendID) -> Unit) ->
            InstrumentsViewModel(get(), navToInstrument)
        } bind Clearable::class
    }
    scope<MainActivity> {
    }
    // TODO: not sure if I should put this here
    single { (saver: BundleStateSaver) ->
        AppViewModel(saver, BottomNavItem.Instruments.pathRoot)
    }
}

val getActivityScope = { getKoin().getOrCreateScope<MainActivity>("MainActivity") }