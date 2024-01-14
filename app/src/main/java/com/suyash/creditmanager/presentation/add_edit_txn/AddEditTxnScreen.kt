package com.suyash.creditmanager.presentation.add_edit_txn

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.suyash.creditmanager.domain.util.TransactionType
import kotlinx.coroutines.flow.collectLatest
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddEditTxnScreen(
    navController: NavController,
    viewModel: AddEditTxnViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    var openDatePickerDialog by rememberSaveable {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditTxnViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditTxnViewModel.UiEvent.NavigateUp -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold (
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (viewModel.currentTxnId == null) "Add Transaction" else "Edit Transaction")
                },
                navigationIcon = {
                    IconButton(onClick = {viewModel.onEvent(AddEditTxnEvent.BackPressed)}) {
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
                onClick = {
                    viewModel.onEvent(AddEditTxnEvent.UpsertTransaction)
                }
            ) {
                Icon(Icons.Filled.Done, "Save Transaction")
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(16.dp)
        ) {
            var ccDropdownExpanded by remember { mutableStateOf(false) }
            var txnTypeDropdownExpanded by remember { mutableStateOf(false) }

            ExposedDropdownMenuBox(
                modifier = Modifier.fillMaxWidth(),
                expanded = ccDropdownExpanded,
                onExpandedChange = {
                    ccDropdownExpanded = !ccDropdownExpanded
                }) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    value = viewModel.creditCards.value.find { it.id == viewModel.selectedCreditCard.value }?.cardName?:"",
                    onValueChange = { },
                    label = { Text("Credit Card") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = ccDropdownExpanded
                        )
                    }
                )
                ExposedDropdownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = ccDropdownExpanded,
                    onDismissRequest = {
                        ccDropdownExpanded = false
                    }
                ) {
                    viewModel.creditCards.value.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.cardName) },
                            onClick = {
                                viewModel.onEvent(AddEditTxnEvent.SelectedCard(it))
                                ccDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
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
                    value = viewModel.txnType.value.name,
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
                                viewModel.onEvent(AddEditTxnEvent.SelectedTxnType(it))
                                txnTypeDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        openDatePickerDialog = true
                    },
                value = viewModel.txnDate.value.format(viewModel.dateFormatter.value),
                readOnly = true,
                enabled = false,
                onValueChange = { },
                label = { Text("Date") },
                colors = OutlinedTextFieldDefaults.colors(
                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = Color.Transparent,
                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledSupportingTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledPrefixColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledSuffixColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.txnAmount.value,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditTxnEvent.EnteredAmount(newText))
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text("Amount") }
            )
            if(openDatePickerDialog) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = TimeUnit.DAYS.toMillis(viewModel.txnDate.value.toEpochDay())
                )
                val confirmEnabled = remember {
                    derivedStateOf { datePickerState.selectedDateMillis != null }
                }

                DatePickerDialog(
                    onDismissRequest = {
                        openDatePickerDialog = false
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                openDatePickerDialog = false
                                viewModel.onEvent(AddEditTxnEvent.EnteredDate(
                                    Instant.ofEpochMilli(
                                        datePickerState.selectedDateMillis?:
                                        TimeUnit.DAYS.toMillis(LocalDate.now().toEpochDay())
                                    ).atZone(ZoneId.systemDefault()).toLocalDate()
                                ))
                            },
                            enabled = confirmEnabled.value
                        ) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                openDatePickerDialog = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}