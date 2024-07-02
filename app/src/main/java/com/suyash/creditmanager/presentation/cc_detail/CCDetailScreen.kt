package com.suyash.creditmanager.presentation.cc_detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.suyash.creditmanager.domain.util.CardType
import com.suyash.creditmanager.presentation.commons.Screen
import com.suyash.creditmanager.presentation.commons.nextBillDate
import com.suyash.creditmanager.presentation.commons.nextDueDate
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CCDetailScreen(
    navController: NavController,
    viewModel: CCDetailViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is CCDetailViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is CCDetailViewModel.UiEvent.NavigateUp -> {
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
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(CCDetailEvent.BackPressed)
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                },
                title = {
                    Text(text = "Credit Card Detail")
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(
                            Screen.AddEditCCScreen.route +
                                    "?ccId=${viewModel.creditCard?.id}"
                        )
                    }) {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = "Edit CC"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = viewModel.creditCard?.cardName ?: "",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (viewModel.creditCard?.cardType == CardType.AMEX) {
                        "XXXX-XXXXXX-X${viewModel.creditCard?.last4Digits}"
                    } else {
                        "XXXX-XXXX-XXXX-${viewModel.creditCard?.last4Digits}"
                    },
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = viewModel.creditCard?.expiryDate?.replaceRange(2,2, "/")?:"",
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Next Bill Date:",
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = nextBillDate(
                        viewModel.creditCard?.billDate?:1,
                        viewModel.dateFormat
                    ),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Next Due Date:",
                    style = MaterialTheme.typography.bodySmall,
                )
                Text(
                    text = nextDueDate(
                        viewModel.creditCard?.billDate?:1,
                        viewModel.creditCard?.dueDate?:1,
                        viewModel.creditCard?.gracePeriod?:false,
                        viewModel.dateFormat
                    ),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}