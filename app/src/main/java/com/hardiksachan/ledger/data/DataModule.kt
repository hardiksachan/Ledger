package com.hardiksachan.ledger.data

import com.hardiksachan.ledger.Database
import com.hardiksachan.ledger.domain.repository.IInstrumentRepository
import com.squareup.sqldelight.android.AndroidSqliteDriver
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single {
        Database(
            AndroidSqliteDriver(
                schema = Database.Schema,
                context = androidContext(),
                name = "ledger.db"
            )
        )
    }

    single {
        InstrumentDataSource(
            get<Database>().insrumentEntityQueries,
            get<Database>().instrumentExpandedQueries,
            get()
        )
    } bind IInstrumentRepository::class
}