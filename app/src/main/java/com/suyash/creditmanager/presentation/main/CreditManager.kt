package com.suyash.creditmanager.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.suyash.creditmanager.presentation.add_edit_cc.AddEditCCScreen
import com.suyash.creditmanager.presentation.add_edit_emi.AddEditEMIScreen
import com.suyash.creditmanager.presentation.add_edit_txn.AddEditTxnScreen
import com.suyash.creditmanager.presentation.cc_detail.CCDetailScreen
import com.suyash.creditmanager.presentation.credit_cards.CreditCardsScreen
import com.suyash.creditmanager.presentation.emi_detail.EMIDetailScreen
import com.suyash.creditmanager.presentation.emis.EMIsScreen
import com.suyash.creditmanager.presentation.settings.SettingsScreen
import com.suyash.creditmanager.presentation.transactions.TransactionsScreen
import com.suyash.creditmanager.presentation.txn_categories.TxnCategoriesScreen
import com.suyash.creditmanager.presentation.commons.Screen

@Composable
fun CreditManager(
    viewModel: CreditManagerViewModel = hiltViewModel()
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                Screen.bottomBarScreens.forEach { screen ->
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route
                    val selected = currentRoute?.startsWith(screen.route) ?: false

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(screen.route) {
                                navController.graph.startDestinationRoute?.let { route ->
                                    popUpTo(route) {
                                        saveState = true
                                    }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            val icon: ImageVector = if (selected) {
                                screen.selectedIcon
                            } else {
                                screen.unselectedIcon
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = screen.title
                            )
                        },
                        label = {
                            if (viewModel.bottomNavLabel.value) {
                                Text(
                                    text = screen.title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = Screen.CreditCardsScreen.route
            ) {
                composable(route = Screen.CreditCardsScreen.route) {
                    CreditCardsScreen(navController = navController)
                }
                composable(
                    route = Screen.TransactionsScreen.route + "?ccId={ccId}",
                    arguments = listOf(
                        navArgument(name = "ccId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) {
                    TransactionsScreen(navController = navController)
                }
                composable(
                    route = Screen.EMIsScreen.route + "?ccId={ccId}",
                    arguments = listOf(
                        navArgument(name = "ccId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) {
                    EMIsScreen(navController = navController)
                }
                composable(route = Screen.SettingsScreen.route) {
                    SettingsScreen(navController = navController)
                }
                composable(
                    route = Screen.AddEditCCScreen.route + "?ccId={ccId}",
                    arguments = listOf(
                        navArgument(name = "ccId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) {
                    AddEditCCScreen(navController = navController)
                }
                composable(
                    route = Screen.CCDetailScreen.route + "?ccId={ccId}",
                    arguments = listOf(
                        navArgument(name = "ccId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) {
                    CCDetailScreen(navController = navController)
                }
                composable(
                    route = Screen.AddEditTxnScreen.route + "?txnId={txnId}",
                    arguments = listOf(
                        navArgument(name = "txnId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) {
                    AddEditTxnScreen(navController = navController)
                }
                composable(
                    route = Screen.AddEditEMIScreen.route + "?emiId={emiId}",
                    arguments = listOf(
                        navArgument(name = "emiId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) {
                    AddEditEMIScreen(navController = navController)
                }
                composable(
                    route = Screen.EMIDetailScreen.route + "?emiId={emiId}",
                    arguments = listOf(
                        navArgument(name = "emiId") {
                            type = NavType.IntType
                            defaultValue = -1
                        }
                    )
                ) {
                    EMIDetailScreen(navController = navController)
                }
                composable(route = Screen.TxnCategoriesScreen.route) {
                    TxnCategoriesScreen(navController = navController)
                }
            }
        }
    }
}