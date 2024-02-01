package com.suyash.creditmanager.presentation.emi_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
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
import com.suyash.creditmanager.presentation.util.CMUtils
import kotlinx.coroutines.flow.collectLatest
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EMIDetailScreen(
    navController: NavController,
    viewModel: EMIDetailViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is EMIDetailViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }
                is EMIDetailViewModel.UiEvent.NavigateUp -> {
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
                    Text(text = "EMI Detail")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(EMIDetailEvent.BackPressed)
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Go Back"
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
                .padding(16.dp)
        ) {
            Text(
                text = viewModel.emi?.name?:"",
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
                    text = "A: " + CMUtils.currencyMask(viewModel.emi?.amount?:0.0F, viewModel.countryCode),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                viewModel.creditCard?.let {
                    Text(
                        text = "${it.cardName} (${it.last4Digits})",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "E: " + CMUtils.currencyMask(viewModel.emiAmount, viewModel.countryCode),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "*Can have rounding errors",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            LazyColumn {
                items(viewModel.schedule) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = it.paymentNumber.toString())
                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "P: " + CMUtils.currencyMask(it.principalAmount.toFloat(), viewModel.countryCode),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "I: " + CMUtils.currencyMask(it.interestAmount.toFloat(), viewModel.countryCode),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        Text(
                            text = CMUtils.currencyMask(it.remainingBalance.toFloat().absoluteValue, viewModel.countryCode),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}