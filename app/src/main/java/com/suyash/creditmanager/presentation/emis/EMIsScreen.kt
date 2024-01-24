package com.suyash.creditmanager.presentation.emis

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.suyash.creditmanager.presentation.util.Screen

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EMIsScreen(
    navController: NavController
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "EMIs") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddEditEMIScreen.route) }
            ) {
                Icon(Icons.Filled.Add, "Add EMI")
            }
        }
    ) {
        paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {

        }
    }
}