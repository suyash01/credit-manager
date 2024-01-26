package com.suyash.creditmanager.presentation.emis

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.suyash.creditmanager.presentation.emis.component.EMIItem
import com.suyash.creditmanager.presentation.util.Screen

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun EMIsScreen(
    navController: NavController,
    viewModel: EMIsViewModel = hiltViewModel()
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
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            items(viewModel.state.value.emis) { emi ->
                EMIItem(
                    emi = emi,
                    countryCode = viewModel.state.value.countryCode,
                    dateFormat = viewModel.state.value.dateFormat,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}