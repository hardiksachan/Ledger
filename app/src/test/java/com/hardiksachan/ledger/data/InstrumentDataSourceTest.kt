package com.hardiksachan.ledger.data

import com.google.common.truth.Truth.assertThat
import com.hardiksachan.ledger.Database
import com.hardiksachan.ledger.TestDispatchers
import com.hardiksachan.ledger.common.IDispatcherProvider
import com.hardiksachan.ledger.data.local.InsrumentEntityQueries
import com.hardiksachan.ledger.data.local.InstrumentExpandedQueries
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class InstrumentDataSourceTest {

    private lateinit var instrumentDataSource: InstrumentDataSource
    private lateinit var expandedQueries: InstrumentExpandedQueries
    private lateinit var baseQueries: InsrumentEntityQueries
    private lateinit var dispatcherProvider: IDispatcherProvider

    @BeforeEach
    fun setUpEach() {
        val inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
            Database.Schema.create(this)
        }

        val db = Database(inMemorySqlDriver)

        baseQueries = db.insrumentEntityQueries
        expandedQueries = db.instrumentExpandedQueries
        dispatcherProvider = TestDispatchers

        instrumentDataSource = InstrumentDataSource(
            baseQueries,
            expandedQueries,
            dispatcherProvider
        )

        baseQueries.addInstrument(
            "001",
            "Physical Wallet",
            500000L,
            1
        )

    }

    @Test
    fun `Add instrument Success`() = runTest {
        val name = "Paytm Wallet"
        val open = 1_000_00L
        val color = 2

        baseQueries.addInstrument(
            "bleh",
            name,
            open,
            color
        )

        val instruments = expandedQueries.getAllInstruments().executeAsList()

        assertThat(instruments.map { it.name }).contains(name)
    }
}