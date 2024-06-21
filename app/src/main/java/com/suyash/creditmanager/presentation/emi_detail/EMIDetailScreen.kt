package com.suyash.creditmanager.presentation.emi_detail

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import com.suyash.creditmanager.presentation.commons.CMUtils
import com.suyash.creditmanager.presentation.commons.Screen
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
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onEvent(EMIDetailEvent.BackPressed)
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back"
                        )
                    }
                },
                title = {
                    Text(text = "EMI Detail")
                },
                actions = {
                    IconButton(onClick = {
                        navController.navigate(
                            Screen.AddEditEMIScreen.route +
                                    "?emiId=${viewModel.emi?.id}"
                        )
                    }) {
                        Icon(
                            Icons.Outlined.Edit,
                            contentDescription = "Edit EMI"
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
                    text = "P: ${CMUtils.currencyMask(viewModel.emi?.amount?:0.0F, viewModel.countryCode)} @ ${viewModel.emi?.rate}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                viewModel.creditCard?.let {
                    Text(
                        text = "${it.cardName} (${it.last4Digits})",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "EMI: ${CMUtils.currencyMask(viewModel.emiAmount, viewModel.countryCode)} X ${viewModel.emi?.months} = ${CMUtils.currencyMask(viewModel.totalAmount, viewModel.countryCode)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Interest: ${CMUtils.currencyMask(viewModel.totalAmount - (viewModel.emi?.amount?:0.0F), viewModel.countryCode)}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            if((viewModel.emi?.taxRate?.toInt() ?: 0) != 0) {
                Text(
                    text = "TToI: ${CMUtils.currencyMask(viewModel.schedule.sumOf { it.taxOnInterest }.toFloat(), viewModel.countryCode)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(
                text = "*Can have rounding errors",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(viewModel.schedule) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = it.paymentNumber.toString())
                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "PP: " + CMUtils.currencyMask(it.principalAmount.toFloat(), viewModel.countryCode),
                                style = MaterialTheme.typography.bodySmall
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "IP: " + CMUtils.currencyMask(it.interestAmount.toFloat(), viewModel.countryCode),
                                style = MaterialTheme.typography.bodySmall
                            )
                            if(it.taxOnInterest.toInt() != 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "ToI: " + CMUtils.currencyMask(it.taxOnInterest.toFloat(), viewModel.countryCode),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
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