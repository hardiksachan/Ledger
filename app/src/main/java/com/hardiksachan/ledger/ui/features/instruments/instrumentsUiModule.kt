package com.hardiksachan.ledger.ui.features.instruments

import androidx.lifecycle.SavedStateHandle
import com.hardiksachan.ledger.presentation_logic.Clearable
import com.hardiksachan.ledger.presentation_logic.instruments.add.AddInstrumentViewModel
import com.hardiksachan.ledger.presentation_logic.instruments.list.InstrumentsViewModel
import com.hardiksachan.ledger.ui.base.scopednav.navigation.navGraphScope
import com.hardiksachan.ledger.ui.features.instruments.add.AddInstrumentScreen
import com.hardiksachan.ledger.ui.features.instruments.list.InstrumentListScreen
import com.hardiksachan.ledger.ui.utils.buildSaver
import org.koin.dsl.bind
import org.koin.dsl.module

val instrumentsUiModule = module {
    navGraphScope<InstrumentsSubgraph> {
    }

    scope<InstrumentListScreen> {
        scoped { (navToAdd: () -> Unit) ->
            InstrumentsViewModel(get(), navToAdd)
        } bind Clearable::class
    }

    scope<AddInstrumentScreen> {
        scoped { (savedState: SavedStateHandle, onDone: () -> Unit) ->
            AddInstrumentViewModel(savedState.buildSaver(), get(), onDone)
        }
    }
}