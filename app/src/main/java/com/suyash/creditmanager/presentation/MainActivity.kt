package com.suyash.creditmanager.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.suyash.creditmanager.presentation.add_edit_cc.AddEditCCScreen
import com.suyash.creditmanager.presentation.credit_cards.CreditCardsScreen
import com.suyash.creditmanager.presentation.util.Screen
import com.suyash.creditmanager.ui.theme.CreditManagerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @ExperimentalMaterial3Api
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CreditManagerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.CreditCardsScreen.route
                    ) {
                        composable(route = Screen.CreditCardsScreen.route) {
                            CreditCardsScreen(navController = navController)
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
                    }
                }
            }
        }
    }
}