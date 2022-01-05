package com.hardiksachan.ledger

import com.hardiksachan.ledger.common.IDispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

object TestDispatchers : IDispatcherProvider {
    override val UI: CoroutineContext
        get() = Dispatchers.IO
    override val IO: CoroutineContext
        get() = Dispatchers.IO
}