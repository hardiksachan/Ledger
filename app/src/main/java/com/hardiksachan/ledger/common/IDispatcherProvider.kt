package com.hardiksachan.ledger.common

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

interface IDispatcherProvider {
    val UI: CoroutineContext
    val IO: CoroutineContext
}

object ProductionDispatcherProvider : IDispatcherProvider {
    override val UI: CoroutineContext
        get() = Dispatchers.Main
    override val IO: CoroutineContext
        get() = Dispatchers.IO
}