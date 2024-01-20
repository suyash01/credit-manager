package com.suyash.creditmanager.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.suyash.creditmanager.presentation.add_edit_cc.AddEditCCScreen
import com.suyash.creditmanager.presentation.add_edit_txn.AddEditTxnScreen
import com.suyash.creditmanager.presentation.credit_cards.CreditCardsScreen
import com.suyash.creditmanager.presentation.settings.SettingsScreen
import com.suyash.creditmanager.presentation.transactions.TransactionsScreen
import com.suyash.creditmanager.presentation.util.Screen

@Composable
fun CreditManager() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                Screen.bottomBarScreens.forEach { screen ->
                    val selected = navController.currentBackStackEntryAsState()
                        .value?.destination?.hierarchy?.any {
                            it.route == screen.route
                        } == true

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.popBackStack()
                            navController.navigate(screen.route)
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
                            Text(text = screen.title)
                        }
                    )
                }
            }
        }
    ) {
        paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(
                navController = navController,
                startDestination = Screen.CreditCardsScreen.route
            ) {
                composable(route = Screen.CreditCardsScreen.route) {
                    CreditCardsScreen(navController = navController)
                }
                composable(route = Screen.TransactionsScreen.route) {
                    TransactionsScreen(navController = navController)
                }
                composable(route = Screen.SettingsScreen.route) {
                    SettingsScreen()
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
            }
        }
    }
}