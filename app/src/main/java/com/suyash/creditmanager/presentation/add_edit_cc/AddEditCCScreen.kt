package com.suyash.creditmanager.presentation.add_edit_cc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.suyash.creditmanager.domain.util.CardType
import com.suyash.creditmanager.presentation.util.CMDateMask
import kotlinx.coroutines.flow.collectLatest

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddEditCCScreen(
    navController: NavController,
    viewModel: AddEditCCViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is AddEditCCViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is AddEditCCViewModel.UiEvent.NavigateUp -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (viewModel.currentCCId.value == 0) "Add Credit Card" else "Edit Credit Card")
                },
                navigationIcon = {
                    IconButton(onClick = {viewModel.onEvent(AddEditCCEvent.BackPressed)}) {
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
                onClick = { viewModel.onEvent(AddEditCCEvent.UpsertCreditCard) }
            ) {
                Icon(Icons.Filled.Done, "Save Credit Card")
            }
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(16.dp)
        ) {
            var ccTypeDropdownExpanded by remember { mutableStateOf(false) }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.cardName.value,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditCCEvent.EnteredCardName(newText))
                },
                label = { Text("Card Name") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ExposedDropdownMenuBox(
                modifier = Modifier.fillMaxWidth(),
                expanded = ccTypeDropdownExpanded,
                onExpandedChange = {
                    ccTypeDropdownExpanded = !ccTypeDropdownExpanded
                }) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    readOnly = true,
                    value = viewModel.cardType.value.name,
                    onValueChange = { },
                    label = { Text("Card Type") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = ccTypeDropdownExpanded
                        )
                    }
                )
                ExposedDropdownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = ccTypeDropdownExpanded,
                    onDismissRequest = {
                        ccTypeDropdownExpanded = false
                    }
                ) {
                    CardType.entries.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.name) },
                            onClick = {
                                viewModel.onEvent(AddEditCCEvent.SelectedCardType(it))
                                ccTypeDropdownExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                value = viewModel.last4Digits.value,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditCCEvent.EnteredLast4Digits(newText))
                },
                prefix = {
                    if (viewModel.cardType.value == CardType.AMEX) {
                        Text("XXXX-XXXXXX-X")
                    } else {
                        Text("XXXX-XXXX-XXXX-")
                    }
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text("Card Number") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.expiry.value,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditCCEvent.EnteredExpiry(newText))
                },
                visualTransformation = CMDateMask(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text("Expiry Date") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.dueDate.value,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditCCEvent.EnteredDueDate(newText))
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text("Due Date") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.billDate.value,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditCCEvent.EnteredBillDate(newText))
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text("Bill Date") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.limit.value,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditCCEvent.EnteredLimit(newText))
                },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                label = { Text("Limit") }
            )
        }
    }
}