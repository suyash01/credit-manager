package com.suyash.creditmanager.presentation.add_edit_emi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.suyash.creditmanager.presentation.commons.components.CustomExposedDropdownMenuBox
import com.suyash.creditmanager.presentation.commons.components.CustomOutlinedTextField
import com.suyash.creditmanager.presentation.commons.visual_transformations.CurrencyTransformation
import kotlinx.coroutines.flow.collectLatest
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Currency
import java.util.Locale
import java.util.concurrent.TimeUnit

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AddEditEMIScreen(
    navController: NavController,
    viewModel: AddEditEMIViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current
    val snackbarHostState = remember { SnackbarHostState() }

    var openDatePickerDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var fabHeight by remember {
        mutableIntStateOf(0)
    }
    val fabHeightInDp = with(LocalDensity.current) { fabHeight.toDp() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditEMIViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is AddEditEMIViewModel.UiEvent.NavigateUp -> {
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
                    Text(text = if (viewModel.currentEMIId.value == -1) "Add EMI" else "Edit EMI")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(AddEditEMIEvent.BackPressed)
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
                modifier = Modifier.onGloballyPositioned { fabHeight = it.size.height },
                onClick = {
                    viewModel.onEvent(AddEditEMIEvent.UpsertEMI)
                }
            ) {
                Icon(Icons.Filled.Done, "Save EMI")
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
            var ccDropdownExpanded by remember { mutableStateOf(false) }

            CustomOutlinedTextField(
                label = "Name*",
                value = viewModel.name.value,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditEMIEvent.EnteredName(newText))
                },
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            CustomOutlinedTextField(
                label = "Amount*",
                value = viewModel.emiAmount.value,
                prefix = Currency.getInstance(Locale("", viewModel.countryCode.value)).symbol,
                visualTransformation = CurrencyTransformation(viewModel.countryCode.value),
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditEMIEvent.EnteredAmount(newText))
                },
                keyboardType = KeyboardType.Number,
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            CustomOutlinedTextField(
                label = "Rate*",
                value = viewModel.interestRate.value,
                suffix = "%",
                visualTransformation = CurrencyTransformation(viewModel.countryCode.value),
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditEMIEvent.EnteredRate(newText))
                },
                keyboardType = KeyboardType.Number,
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            CustomOutlinedTextField(
                label = "Months*",
                value = viewModel.months.value,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditEMIEvent.EnteredMonths(newText))
                },
                keyboardType = KeyboardType.Number,
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        openDatePickerDialog = true
                    },
                value = viewModel.emiDate.value.format(viewModel.dateFormatter.value),
                readOnly = true,
                enabled = false,
                onValueChange = { },
                label = { Text("Start Date") },
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
            CustomExposedDropdownMenuBox(
                expanded = ccDropdownExpanded,
                onExpandedChange = { ccDropdownExpanded = !ccDropdownExpanded },
                value = viewModel.getCCDisplay(),
                entries = viewModel.creditCards.value.map { Pair(it.id, "${it.cardName} (${it.last4Digits})") },
                onClick = {
                    viewModel.onEvent(AddEditEMIEvent.SelectedCard(it))
                    ccDropdownExpanded = false
                },
                label = "Credit Card"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomOutlinedTextField(
                label = "Rate*",
                value = viewModel.taxRate.value,
                suffix = "%",
                visualTransformation = CurrencyTransformation(viewModel.countryCode.value),
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditEMIEvent.EnteredTaxRate(newText))
                },
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone =  { focusManager.clearFocus() })
            )
            Spacer(modifier = Modifier.height(fabHeightInDp))
            if (openDatePickerDialog) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = TimeUnit.DAYS.toMillis(viewModel.emiDate.value.toEpochDay())
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
                                viewModel.onEvent(
                                    AddEditEMIEvent.EnteredStartDate(
                                        Instant.ofEpochMilli(
                                            datePickerState.selectedDateMillis
                                                ?: TimeUnit.DAYS.toMillis(
                                                    LocalDate.now().toEpochDay()
                                                )
                                        ).atZone(ZoneId.systemDefault()).toLocalDate()
                                    )
                                )
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