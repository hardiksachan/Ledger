// Inspired by https://github.com/lotdrops/Composing-Clocks

package com.hardiksachan.ledger.ui

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.hardiksachan.ledger.R
import com.hardiksachan.ledger.presentation_logic.AppViewModel
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
import org.koin.androidx.compose.get

private val startDestinationPath = InstrumentsScreen.declaredPath

@ExperimentalAnimationApi
@Composable
fun RootNavigation(navController: NavHostController) {
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
                // TODO
            }
            val viewModel = scope.getWith<InstrumentsViewModel>(navigate)
            InstrumentsScreen(
                viewModel = viewModel,
                bottomBar = { BottomBar(navController = navController) }
            )
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

private fun updateStateIfStartDestination(destination: NavDestination?, vm: AppViewModel) {
    if (destination?.id != null && destination.id == destination.getRootGraph()?.startDestinationId) {
        vm.onBottomDestinationChanged(startDestinationPath)
    }
}

private fun NavDestination.getRootGraph(): NavGraph? {
    var parentGraph = parent
    while (parentGraph?.parent != null) {
        parentGraph = parentGraph.parent
    }
    return parentGraph
}

@Composable
fun BottomBar(navController: NavHostController) {
    val appVm: AppViewModel = get()
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val bottomBarSelection: String by appVm.bottomBarSelection.collectAsState()

        val items = listOf(
            BottomNavItem.Instruments,
            // TODO: add more items
        )

        updateStateIfStartDestination(navBackStackEntry?.destination, appVm)

        items.forEach { screen ->
            val route = screen.route

            BottomNavigationItem(
                icon = { Icon(screen.icon, stringResource(screen.title)) },
                label = { Text(stringResource(screen.title)) },
                selected = bottomBarSelection == route,
                onClick = {
                    appVm.bottomBarSelection.value = route
                    navController.navigate(route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            )
        }
    }
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