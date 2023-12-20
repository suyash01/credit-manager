package com.suyash.creditmanager.presentation.transactions

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.suyash.creditmanager.presentation.util.Screen

@Composable
fun TransactionsScreen(
    navController: NavController
) {
    Scaffold (
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddEditCCScreen.route) }
            ) {
                Icon(Icons.Filled.Add, "Add Credit Card")
            }
        }
    ) { contentPadding ->
        Text(text = "Transactions", modifier = Modifier.padding(contentPadding))
    }
}