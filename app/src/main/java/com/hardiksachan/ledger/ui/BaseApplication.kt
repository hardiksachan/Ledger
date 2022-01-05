package com.hardiksachan.ledger.ui

import android.app.Application
import com.hardiksachan.ledger.common.commonModule
import com.hardiksachan.ledger.data.dataModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BaseApplication)
            dataModule.single { applicationScope }
            commonModule.single { applicationScope }
            modules(
                commonModule,
                dataModule,
                uiModule
            )
        }
    }
}