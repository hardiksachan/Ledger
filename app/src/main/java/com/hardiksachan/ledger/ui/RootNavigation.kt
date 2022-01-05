package com.hardiksachan.ledger.ui

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.hardiksachan.ledger.R
import com.hardiksachan.ledger.presentation_logic.instruments.list.InstrumentsViewModel
import com.hardiksachan.ledger.ui.base.getEnterTransition
import com.hardiksachan.ledger.ui.base.getExitTransition
import com.hardiksachan.ledger.ui.base.getPopEnterTransition
import com.hardiksachan.ledger.ui.base.getPopExitTransition
import com.hardiksachan.ledger.ui.base.scopednav.navigation.NoParams
import com.hardiksachan.ledger.ui.base.scopednav.navigation.getWith
import com.hardiksachan.ledger.ui.base.scopednav.navigation.scopedComposable
import com.hardiksachan.ledger.ui.features.instruments.list.InstrumentsScreen
import com.hardiksachan.ledger.ui.theme.LocalMotionTransition

private val startDestinationPath = InstrumentsScreen.declaredPath

private const val TAG = "RootNavigation"

@ExperimentalAnimationApi
@Composable
fun RootNavigation(navController: NavHostController) {
    val screenWidth = with(LocalDensity.current) {
        LocalConfiguration.current.screenWidthDp.dp.toPx()
    }.toInt()

    val motionTransition = LocalMotionTransition.current

    AnimatedNavHost(
        navController = navController,
        startDestination = startDestinationPath,
        enterTransition = {
            getEnterTransition(motionTransition)
        },
        exitTransition = {
            getExitTransition(motionTransition)
        },
        popEnterTransition = {
            getPopEnterTransition(motionTransition)
        },
        popExitTransition = {
            getPopExitTransition(motionTransition)
        },
    ) {


        scopedComposable(
            InstrumentsScreen,
        ) { _, scope ->
            val navigate = { id: Int ->
                Log.d(TAG, "RootNavigation: navigate to Instrument Screen")
            }
            val viewModel = scope.getWith<InstrumentsViewModel>(navigate)
            InstrumentsScreen(viewModel)
        }
    }
}

private fun navToTopDestination(
    path: String,
    navController: NavController
) {
    navController.navigate(path) {
        popUpTo(navController.graph.startDestinationId)
    }
}

private fun updateStateIfStartDestination(destination: NavDestination?) {

}

private fun NavDestination.getRootGraph(): NavGraph? {
    var parentGraph = parent
    while (parentGraph?.parent != null) {
        parentGraph = parentGraph.parent
    }
    return parentGraph
}

sealed class BottomNavItem(
    val pathRoot: String,
    val route: String,
    @StringRes val title: Int,
    val icon: ImageVector,
) {

    object Instruments : BottomNavItem(
        InstrumentsScreen.declaredPath,
        InstrumentsScreen.buildRoute(NoParams),
        R.string.instruments_screen_title,
        Icons.Filled.List,
    )
}