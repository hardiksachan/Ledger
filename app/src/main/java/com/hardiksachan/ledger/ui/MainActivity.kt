package com.hardiksachan.ledger.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.hardiksachan.ledger.presentation_logic.AppViewModel
import com.hardiksachan.ledger.ui.base.savestate.BundleStateSaver
import com.hardiksachan.ledger.ui.theme.LedgerTheme
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf

private const val StateSaverKey = "GlobalStateSaverKey"

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val stateSaver = rememberSaveable(
                key = StateSaverKey,
                saver = BundleStateSaver.saver,
            ) { BundleStateSaver(Bundle()) }

            get<AppViewModel> { parametersOf(stateSaver) }

            LedgerTheme {
                AppLayout()
            }
        }
    }

    override fun onDestroy() {
        getActivityScope().close()
        super.onDestroy()
    }
}

@ExperimentalAnimationApi
@Composable
fun AppLayout() {
    val navController = rememberAnimatedNavController()
    RootNavigation(navController)
}