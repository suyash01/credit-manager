package com.suyash.creditmanager.presentation.transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.suyash.creditmanager.presentation.transactions.component.TransactionItem
import com.suyash.creditmanager.presentation.commons.Screen
import com.suyash.creditmanager.presentation.commons.components.ConfirmationDialog

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun TransactionsScreen(
    navController: NavController,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val haptics = LocalHapticFeedback.current
    val bottomSheetState = rememberModalBottomSheetState()
    var isBottomSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var openDeleteConfirmationDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Transactions") },
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddEditTxnScreen.route) }
            ) {
                Icon(Icons.Filled.Add, "Add Transaction")
            }
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier.padding(contentPadding)
        ) {
            val groupedTxn = viewModel.state.value.transactions.groupBy { it.date }
            groupedTxn.forEach { (date, transactions) ->
                stickyHeader {
                    Box(modifier = Modifier.padding(bottom = 16.dp)) {
                        Text(
                            text = date.format(viewModel.state.value.dateFormat.formatter),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(horizontal = 16.dp)
                        )
                    }
                }
                items(transactions) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        creditCard = viewModel.state.value.creditCards[transaction.card],
                        countryCode = viewModel.state.value.countryCode,
                        modifier = Modifier
                            .fillMaxWidth()
                            .combinedClickable(
                                onClick = { },
                                onLongClick = {
                                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                    viewModel.onEvent(
                                        TransactionsEvent.ToggleBottomSheet(
                                            transaction
                                        )
                                    )
                                    isBottomSheetOpen = true
                                }
                            )
                    )
                }
            }
        }
        if (openDeleteConfirmationDialog) {
            ConfirmationDialog(
                icon = Icons.Filled.Delete,
                title = "Delete Transaction?",
                description = "Do you want to delete the transaction?",
                onDismissRequest = { openDeleteConfirmationDialog = false },
                onConfirmButton = {
                    openDeleteConfirmationDialog = false
                    viewModel.onEvent(
                        TransactionsEvent.DeleteTransaction(
                            viewModel.state.value.selectedTransaction
                        )
                    )
                },
                onDismissButton = { openDeleteConfirmationDialog = false }
            )
        }
        if (isBottomSheetOpen) {
            ModalBottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = { isBottomSheetOpen = false }
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clickable {
                                isBottomSheetOpen = false
                                navController.navigate(
                                    Screen.AddEditTxnScreen.route +
                                            "?txnId=${viewModel.state.value.selectedTransaction?.id}"
                                )
                            }
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Icon(Icons.Filled.Edit, "Edit Txn")
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
                        Icon(Icons.Filled.Delete, "Delete Txn")
                        Text(text = "Delete", modifier = Modifier.padding(start = 16.dp))
                    }
                }
            }
        }
    }
}