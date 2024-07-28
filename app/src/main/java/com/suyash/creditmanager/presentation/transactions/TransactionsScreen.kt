package com.suyash.creditmanager.presentation.transactions

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.suyash.creditmanager.domain.util.order.TransactionOrder
import com.suyash.creditmanager.presentation.transactions.component.TransactionItem
import com.suyash.creditmanager.presentation.commons.Screen
import com.suyash.creditmanager.presentation.commons.components.CustomActionBottomSheet
import com.suyash.creditmanager.presentation.commons.components.CustomConfirmationDialog
import com.suyash.creditmanager.presentation.commons.components.CustomSortingBottomSheet
import com.suyash.creditmanager.presentation.commons.model.ItemAction

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun TransactionsScreen(
    navController: NavController,
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val haptics = LocalHapticFeedback.current
    var isItemBottomSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var isSortBottomSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var openDeleteConfirmationDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var fabHeight by remember {
        mutableIntStateOf(0)
    }
    val fabHeightInDp = with(LocalDensity.current) { fabHeight.toDp() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Transactions") },
                actions = {
                    IconButton(onClick = { isSortBottomSheetOpen = true }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "Sort Transactions"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.onGloballyPositioned { fabHeight = it.size.height },
                onClick = { navController.navigate(Screen.AddEditTxnScreen.route) }
            ) {
                Icon(Icons.Filled.Add, "Add Transaction")
            }
        }
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding),
            contentPadding = PaddingValues(
                bottom = fabHeightInDp + 16.dp
            )
        ) {
            val groupedTxn = viewModel.state.value.transactions.groupBy { it.date }
            groupedTxn.forEach { (date, transactions) ->
                stickyHeader {
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
                                    isItemBottomSheetOpen = true
                                }
                            )
                    )
                }
            }
        }
        if (openDeleteConfirmationDialog) {
            CustomConfirmationDialog(
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
        if (isItemBottomSheetOpen) {
            CustomActionBottomSheet(
                onDismissRequest = { isItemBottomSheetOpen = false },
                actions = listOf(
                    ItemAction(
                        icon = Icons.Filled.Edit,
                        iconName = "Edit Transaction",
                        title = "Edit",
                        onClick = {
                            navController.navigate(
                                Screen.AddEditCCScreen.route +
                                        "?ccId=${viewModel.state.value.selectedTransaction?.id}"
                            )
                        }
                    ),
                    ItemAction(
                        icon = Icons.Filled.Delete,
                        iconName = "Delete Transaction",
                        title = "Delete",
                        onClick = {
                            openDeleteConfirmationDialog = true
                        }
                    )
                )
            )
        }
        if (isSortBottomSheetOpen) {
            CustomSortingBottomSheet(
                onDismissRequest = { isSortBottomSheetOpen = false },
                orderList = TransactionOrder.getOrderList(),
                currentOrder = viewModel.state.value.transactionOrder,
                sort = { viewModel.onEvent(TransactionsEvent.Order(it)) }
            )
        }
    }
}