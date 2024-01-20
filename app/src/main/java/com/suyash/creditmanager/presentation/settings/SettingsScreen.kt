package com.suyash.creditmanager.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.suyash.creditmanager.BuildConfig
import com.suyash.creditmanager.domain.util.DateFormat
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uriHandler: UriHandler = LocalUriHandler.current
    var openCurrencyDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var openDateFormatDialog by rememberSaveable {
        mutableStateOf(false)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Settings") },
            )
        }
    ) {
        paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        openCurrencyDialog = true
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Payments,
                    contentDescription = "Currency Country",
                    modifier = Modifier.padding(start = 16.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Currency Country",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${viewModel.countryCode.value} - ${
                            Locale(
                                "",
                                viewModel.countryCode.value
                            ).displayName
                        }",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        openDateFormatDialog = true
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.CalendarToday,
                    contentDescription = "Date Format",
                    modifier = Modifier.padding(start = 16.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Date Format",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = viewModel.dateFormat.value.format,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        uriHandler.openUri("https://github.com/suyash01/credit-manager/releases")
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.Info,
                    contentDescription = "Check for Updates",
                    modifier = Modifier.padding(start = 16.dp)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Check for Updates",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "v${BuildConfig.VERSION_NAME}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }

        if (openCurrencyDialog) {
            Dialog(
                onDismissRequest = {
                    openCurrencyDialog = false
                }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .selectableGroup()
                            .weight(1f)
                    ) {
                        itemsIndexed(viewModel.countries.value) { _, it ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .selectable(
                                        selected = (it.country == viewModel.countryCode.value),
                                        onClick = {
                                            viewModel.onEvent(
                                                SettingsEvent.UpdateCurrency(
                                                    it.country
                                                )
                                            )
                                        },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (it.country == viewModel.countryCode.value),
                                    onClick = null
                                )
                                Text(
                                    text = "${it.country} - ${it.displayName}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                    TextButton(
                        onClick = {
                            openCurrencyDialog = false
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
        if (openDateFormatDialog) {
            Dialog(
                onDismissRequest = {
                    openDateFormatDialog = false
                }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .selectableGroup()
                    ) {
                        itemsIndexed(DateFormat.entries) { _, it ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .selectable(
                                        selected = (it == viewModel.dateFormat.value),
                                        onClick = {
                                            viewModel.onEvent(
                                                SettingsEvent.UpdateDateFormat(it)
                                            )
                                        },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (it == viewModel.dateFormat.value),
                                    onClick = null
                                )
                                Text(
                                    text = it.format,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                    TextButton(
                        onClick = {
                            openDateFormatDialog = false
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("Close")
                    }
                }
            }
        }
    }
}