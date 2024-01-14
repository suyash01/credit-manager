package com.suyash.creditmanager.presentation.credit_cards

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.suyash.creditmanager.presentation.credit_cards.component.CreditCardItem
import com.suyash.creditmanager.presentation.util.Screen

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun CreditCardsScreen(
    navController: NavController,
    viewModel: CreditCardsViewModel = hiltViewModel()
) {
    val haptics = LocalHapticFeedback.current
    val bottomSheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var openDeleteConfirmationDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text(text = "Credit Cards") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddEditCCScreen.route) }
            ) {
                Icon(Icons.Filled.Add, "Add Credit Card")
            }
        }
    ) {
        contentPadding ->
        LazyColumn (
            modifier = Modifier.padding(contentPadding)
        ) {
            items(viewModel.state.value.creditCards) { creditCard ->
                CreditCardItem(
                    creditCard = creditCard,
                    countryCode = viewModel.state.value.countryCode,
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = { },
                            onLongClick = {
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.onEvent(CreditCardsEvent.ToggleBottomSheet(creditCard))
                                isBottomSheetOpen = true
                            }
                        )
                )
            }
        }
        if(openDeleteConfirmationDialog) {
            AlertDialog(
                icon = {
                    Icon(Icons.Filled.Delete, "Delete CC")
                },
                title = {
                    Text(text = "Delete Credit Card?")
                },
                text = {
                    Text(text = "Do you want to delete ${viewModel.state.value.selectedCreditCard?.cardName}")
                },
                onDismissRequest = {
                    openDeleteConfirmationDialog = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDeleteConfirmationDialog = false
                            viewModel.onEvent(
                                CreditCardsEvent.DeleteCreditCard(
                                    viewModel.state.value.selectedCreditCard
                                )
                            )
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDeleteConfirmationDialog = false
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            )
        }
        if(isBottomSheetOpen) {
            ModalBottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = { isBottomSheetOpen = false }
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .clickable {
                                isBottomSheetOpen = false
                                navController.navigate(
                                    Screen.AddEditCCScreen.route +
                                            "?ccId=${viewModel.state.value.selectedCreditCard?.id}"
                                )
                            }
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Icon(Icons.Filled.Edit, "Edit CC")
                        Text(text = "Edit", modifier = Modifier.padding(start = 16.dp))
                    }
                    Row(
                        modifier = Modifier
                            .clickable {
                                isBottomSheetOpen = false
                                openDeleteConfirmationDialog = true
                            }
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Icon(Icons.Filled.Delete, "Delete CC")
                        Text(text = "Delete", modifier = Modifier.padding(start = 16.dp))
                    }
                }
            }
        }
    }
}