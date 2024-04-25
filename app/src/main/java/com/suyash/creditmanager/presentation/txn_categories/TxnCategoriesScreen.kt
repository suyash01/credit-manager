package com.suyash.creditmanager.presentation.txn_categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.suyash.creditmanager.domain.model.TxnCategory
import com.suyash.creditmanager.domain.util.TransactionType
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TxnCategoriesScreen(
    navController: NavController,
    viewModel: TxnCategoriesViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var openCategoryDialog by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is TxnCategoriesViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is TxnCategoriesViewModel.UiEvent.NavigateUp -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Transaction Categories")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(TxnCategoriesEvent.BackPressed)
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(TxnCategoriesEvent.CategorySelected(TxnCategory(TransactionType.DEBIT, "")))
                    openCategoryDialog = true
                }
            ) {
                Icon(Icons.Filled.Add, "Add Category")
            }
        }
    ) { paddingValues ->
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 8.dp)
                .verticalScroll(scrollState)
                .fillMaxWidth()
        ) {
            TransactionType.entries.forEach { txnType ->
                Column (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = txnType.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(8.dp)
                    )
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        viewModel.txnCategories.value.filter { txnType == it.type }.forEach {
                            SuggestionChip(
                                onClick = {
                                    viewModel.onEvent(TxnCategoriesEvent.CategorySelected(it))
                                    openCategoryDialog = true
                                },
                                label = { Text(text = it.name) },
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                        }
                    }
                }
            }
        }
        if(openCategoryDialog) {
            Dialog(onDismissRequest = { openCategoryDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    var txnTypeDropdownExpanded by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        ExposedDropdownMenuBox(
                            modifier = Modifier.fillMaxWidth(),
                            expanded = txnTypeDropdownExpanded,
                            onExpandedChange = {
                                txnTypeDropdownExpanded = !txnTypeDropdownExpanded
                            }) {
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                readOnly = true,
                                value = viewModel.type.value.name,
                                onValueChange = { },
                                label = { Text("Transaction Type") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(
                                        expanded = txnTypeDropdownExpanded
                                    )
                                }
                            )
                            ExposedDropdownMenu(
                                modifier = Modifier.fillMaxWidth(),
                                expanded = txnTypeDropdownExpanded,
                                onDismissRequest = {
                                    txnTypeDropdownExpanded = false
                                }
                            ) {
                                TransactionType.entries.forEach {
                                    DropdownMenuItem(
                                        text = { Text(text = it.name) },
                                        onClick = {
                                            viewModel.onEvent(TxnCategoriesEvent.TypeSelected(it))
                                            txnTypeDropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = viewModel.name.value,
                            onValueChange = { newText ->
                                viewModel.onEvent(TxnCategoriesEvent.NameEntered(newText))
                            },
                            label = { Text("Category Name") }
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = if(viewModel.selectedId.value == 0)
                                                        Arrangement.End
                                                    else
                                                        Arrangement.SpaceBetween
                        ) {
                            if (viewModel.selectedId.value != 0) {
                                TextButton(
                                    onClick = {
                                        viewModel.onEvent(TxnCategoriesEvent.DeleteCategory)
                                        openCategoryDialog = false
                                    }
                                ) {
                                    Text("Delete")
                                }
                            }
                            Row(
                                horizontalArrangement = Arrangement.End
                            ) {
                                TextButton(
                                    onClick = {
                                        openCategoryDialog = false
                                    }
                                ) {
                                    Text("Close")
                                }
                                TextButton(
                                    onClick = {
                                        viewModel.onEvent(TxnCategoriesEvent.UpsertCategory)
                                        openCategoryDialog = false
                                    }
                                ) {
                                    Text("Save")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}