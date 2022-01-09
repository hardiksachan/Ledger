// Inspired by https://github.com/lotdrops/Composing-Clocks

package com.hardiksachan.ledger.ui

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Payment
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
import com.hardiksachan.ledger.presentation_logic.instruments.add.AddInstrumentViewModel
import com.hardiksachan.ledger.presentation_logic.instruments.list.InstrumentsViewModel
import com.hardiksachan.ledger.ui.base.*
import com.hardiksachan.ledger.ui.base.scopednav.navigation.NoParams
import com.hardiksachan.ledger.ui.base.scopednav.navigation.doubleScopedComposable
import com.hardiksachan.ledger.ui.base.scopednav.navigation.scopedNavigation
import com.hardiksachan.ledger.ui.features.instruments.InstrumentsSubgraph
import com.hardiksachan.ledger.ui.features.instruments.add.AddInstrumentScreen
import com.hardiksachan.ledger.ui.features.instruments.list.InstrumentListScreen
import com.hardiksachan.ledger.ui.theme.LocalMotionTransition
import org.koin.androidx.compose.get
import org.koin.core.parameter.parametersOf

private val startDestinationPath = InstrumentsSubgraph.declaredPath

@ExperimentalAnimationApi
@Composable
fun RootNavigation(navController: NavHostController) {
    val appVm: AppViewModel = get()
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
        scopedNavigation(InstrumentsSubgraph) { nestedNavGraph ->

            doubleScopedComposable(
                navController, nestedNavGraph, InstrumentListScreen
            ) { _, _, scope ->
                val navToAdd: () -> Unit = {
                    navController.navigate(
                        AddInstrumentScreen.buildRoute(NoParams, NavTransition.FadeThrough)
                    )
                }

                val vm = scope.get<InstrumentsViewModel> {
                    parametersOf(navToAdd)
                }

                InstrumentListScreen(viewModel = vm) {
                    BottomBar(navController = navController)
                }
            }

            doubleScopedComposable(
                navController, nestedNavGraph, AddInstrumentScreen
            ) { navEntry, _, scope ->
                val onDone: () -> Unit = {
                    navController.navigateUp()
                }

                val vm = scope.get<AddInstrumentViewModel> {
                    parametersOf(navEntry.savedStateHandle, onDone)
                }

                AddInstrumentScreen(vm) {
                    BottomBar(navController = navController)
                }
            }
        }
    }
}

private fun navToTopDestination(
    path: String,
    navController: NavController,
    appViewModel: AppViewModel,
) {
    navController.navigate(path) {
        popUpTo(navController.graph.startDestinationId)
    }
    appViewModel.onBottomDestinationChanged(path)
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
    BottomNavigation(
        backgroundColor = MaterialTheme.colors.background
    ) {
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
        InstrumentsSubgraph.declaredPath,
        InstrumentsSubgraph.buildRoute(NoParams),
        R.string.instruments_screen_title,
        Icons.Filled.Payment,
    )
}