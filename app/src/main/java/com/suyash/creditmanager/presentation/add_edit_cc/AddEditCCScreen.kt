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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.suyash.creditmanager.presentation.commons.visual_transformations.CCExpiryTransformation
import com.suyash.creditmanager.presentation.commons.components.CustomExposedDropdownMenuBox
import com.suyash.creditmanager.presentation.commons.components.CustomOutlinedTextField
import com.suyash.creditmanager.presentation.commons.visual_transformations.CurrencyTransformation
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

    var fabHeight by remember {
        mutableIntStateOf(0)
    }
    val fabHeightInDp = with(LocalDensity.current) { fabHeight.toDp() }

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

            CustomExposedDropdownMenuBox(
                expanded = ccTypeDropdownExpanded,
                onExpandedChange = {
                    ccTypeDropdownExpanded = !ccTypeDropdownExpanded
                },
                value = viewModel.cardType.value.name,
                entries = CardType.entries.map { Pair(it.name, it.name) },
                onClick = {
                    viewModel.onEvent(AddEditCCEvent.SelectedCardType(it))
                    ccTypeDropdownExpanded = false
                },
                label = "Card Type"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomOutlinedTextField(
                label = "Card Name* ",
                value = viewModel.cardName.value,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditCCEvent.EnteredCardName(newText))
                },
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            CustomOutlinedTextField(
                label = "Card Number*",
                prefix = if (viewModel.cardType.value == CardType.AMEX) {
                    "XXXX-XXXXXX-X"
                } else {
                    "XXXX-XXXX-XXXX-"
                },
                value = viewModel.last4Digits.value,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditCCEvent.EnteredLast4Digits(newText))
                },
                keyboardType = KeyboardType.Number,
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            CustomOutlinedTextField(
                label = "Expiry Date*",
                placeholder = "MM/YY",
                value = viewModel.expiry.value,
                visualTransformation = CCExpiryTransformation(),
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditCCEvent.EnteredExpiry(newText))
                },
                keyboardType = KeyboardType.Number,
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(
                    checked = viewModel.gracePeriod.value,
                    onCheckedChange = { viewModel.onEvent(AddEditCCEvent.CheckedGracePeriod(it)) }
                )
                Text(
                    "Grace period instead of due date"
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomOutlinedTextField(
                    modifier = Modifier.weight(1f),
                    label = "Bill Date*",
                    value = viewModel.billDate.value,
                    onValueChange = { newText ->
                        viewModel.onEvent(AddEditCCEvent.EnteredBillDate(newText))
                    },
                    keyboardType = KeyboardType.Number,
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Right) })
                )
                CustomOutlinedTextField(
                    modifier = Modifier.weight(1f),
                    label = if (viewModel.gracePeriod.value) "Grace Period*" else "Due Date*",
                    value = viewModel.dueDate.value,
                    onValueChange = { newText ->
                        viewModel.onEvent(AddEditCCEvent.EnteredDueDate(newText))
                    },
                    keyboardType = KeyboardType.Number,
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )
            }
            CustomOutlinedTextField(
                label = "Card Limit*",
                prefix = Currency.getInstance(Locale("", viewModel.countryCode.value)).symbol,
                value = viewModel.limit.value,
                visualTransformation = CurrencyTransformation(viewModel.countryCode.value, true, 0),
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditCCEvent.EnteredLimit(newText))
                },
                keyboardType = KeyboardType.Number,
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )
            CustomOutlinedTextField(
                label = "Bank Name",
                value = viewModel.bankName.value,
                onValueChange = { newText ->
                    viewModel.onEvent(AddEditCCEvent.EnteredBankName(newText))
                },
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
            )
            Spacer(modifier = Modifier.height(fabHeightInDp))
        }
    }
}
