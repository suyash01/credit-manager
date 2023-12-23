package com.suyash.creditmanager.presentation.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import java.util.Locale

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    var openCurrencyDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val scrollState = rememberScrollState()
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                openCurrencyDialog = true
            }
    ) {
        Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
        ) {
            Text(
                text = "Country",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${viewModel.countryCode.value} - ${Locale("", viewModel.countryCode.value).displayName}",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }

    if(openCurrencyDialog) {
        Dialog(
            onDismissRequest = {
                openCurrencyDialog = false
            }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                FlowRow(
                    modifier = Modifier
                        .verticalScroll(scrollState)
                        .selectableGroup()
                        .weight(1f)
                ) {
                    Locale.getISOCountries().forEach {
                        val locale = Locale("", it)
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (locale.country == viewModel.countryCode.value),
                                    onClick = { viewModel.onEvent(SettingsEvent.UpdateCurrency(locale.country)) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (locale.country == viewModel.countryCode.value),
                                onClick = null
                            )
                            Text(
                                text = "${locale.country} - ${locale.displayName}",
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
                    Text("Cancel")
                }
            }
        }
    }
}