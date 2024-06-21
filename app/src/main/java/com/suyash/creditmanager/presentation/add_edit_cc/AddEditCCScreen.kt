package com.suyash.creditmanager.presentation.add_edit_cc

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Checkbox
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.suyash.creditmanager.domain.util.CardType
import com.suyash.creditmanager.presentation.commons.CMDateMask
import kotlinx.coroutines.flow.collectLatest
import java.util.Currency
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddEditCCScreen(
    navController: NavController,
    viewModel: AddEditCCViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
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

    var fabHeight by remember {
        mutableIntStateOf(0)
    }
    val fabHeightInDp = with(LocalDensity.current) { fabHeight.toDp() }

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
                    IconButton(onClick = { viewModel.onEvent(AddEditCCEvent.BackPressed) }) {
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
                modifier = Modifier.onGloballyPositioned { fabHeight = it.size.height },
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
                .verticalScroll(rememberScrollState())
        ) {
            var ccTypeDropdownExpanded by remember { mutableStateOf(false) }

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
                value = viewModel.cardName.value,
                singleLine = true,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditCCEvent.EnteredCardName(newText))
                },
                label = { Text("Card Name") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
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
                label = { Text("Card Number") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.expiry.value,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditCCEvent.EnteredExpiry(newText))
                },
                visualTransformation = CMDateMask(),
                label = { Text("Expiry Date") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = viewModel.gracePeriod.value,
                    onCheckedChange = { viewModel.onEvent(AddEditCCEvent.CheckedGracePeriod(it)) }
                )
                Text(
                    "Grace period instead of bill date"
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = viewModel.dueDate.value,
                    onValueChange = { newText ->
                        viewModel.onEvent(AddEditCCEvent.EnteredDueDate(newText))
                    },
                    label = { Text("Due Date") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    modifier = Modifier.weight(1f),
                    value = viewModel.billDate.value,
                    onValueChange = { newText ->
                        viewModel.onEvent(AddEditCCEvent.EnteredBillDate(newText))
                    },
                    label = { Text(if(viewModel.gracePeriod.value) "Grace Period" else "Bill Date") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.limit.value,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditCCEvent.EnteredLimit(newText))
                },
                prefix = {
                    Text(
                        text = Currency.getInstance(
                            Locale(
                                "",
                                viewModel.countryCode.value
                            )
                        ).symbol
                    )
                },
                label = { Text("Limit") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().padding(bottom = fabHeightInDp),
                value = viewModel.bankName.value,
                singleLine = true,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditCCEvent.EnteredBankName(newText))
                },
                label = { Text("Bank Name") },
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
        }
    }
}
