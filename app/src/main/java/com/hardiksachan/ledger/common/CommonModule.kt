package com.hardiksachan.ledger.common

import org.koin.dsl.bind
import org.koin.dsl.module

val commonModule = module {
    single { ProductionDispatcherProvider } bind IDispatcherProvider::class
}