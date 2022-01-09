package com.hardiksachan.ledger.ui.features.instruments

import com.hardiksachan.ledger.ui.base.scopednav.navigation.NoParams
import com.hardiksachan.ledger.ui.base.scopednav.navigation.SubgraphDestination
import com.hardiksachan.ledger.ui.features.instruments.list.InstrumentListScreen

object InstrumentsSubgraph : SubgraphDestination<NoParams, NoParams>(
    pathRoot = "instrumentsSubgraph",
    startDestination = InstrumentListScreen,
)