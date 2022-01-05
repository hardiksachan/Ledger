package com.hardiksachan.ledger.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.hardiksachan.ledger.ui.base.savestate.BundleStateSaver
import com.hardiksachan.ledger.ui.theme.LedgerTheme

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

//            val appVm: AppViewModel = get { parametersOf(stateSaver) }

//            get<IInstrumentRepository> { parametersOf(stateSaver) }

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
    Scaffold() { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            RootNavigation(navController)
        }
    }
}