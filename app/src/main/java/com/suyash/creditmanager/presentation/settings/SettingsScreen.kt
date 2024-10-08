package com.suyash.creditmanager.presentation.settings

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MultiChoiceSegmentedButtonRow
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.suyash.creditmanager.BuildConfig
import com.suyash.creditmanager.data.backup.BackupWorker
import com.suyash.creditmanager.domain.util.DateFormat
import com.suyash.creditmanager.presentation.commons.Screen
import com.suyash.creditmanager.presentation.settings.component.SettingsItem
import com.suyash.creditmanager.presentation.settings.component.SettingsToggle
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uriHandler: UriHandler = LocalUriHandler.current
    val snackbarHostState = remember { SnackbarHostState() }
    var openCurrencyDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var openDateFormatDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val chooseBackupFile = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json"),
    ) {
        if (it != null) {
            context.contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION or
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION,
            )
            BackupWorker.startNow(context, it, "BACKUP")
            viewModel.onEvent(SettingsEvent.ShowSnackbar("Backup job started"))
        }
    }

    val chooseRestoreFile = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) {
        if (it != null) {
            BackupWorker.startNow(context, it, "RESTORE")
            viewModel.onEvent(SettingsEvent.ShowSnackbar("Restore job started"))
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is SettingsViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is SettingsViewModel.UiEvent.NavigateUp -> {
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
                title = { Text(text = "Settings") },
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = contentPadding.calculateTopPadding(),
                    bottom = contentPadding.calculateBottomPadding()
                )
        ) {
            SettingsItem(
                icon = Icons.Outlined.Payments,
                title = "Currency Country",
                description = "${viewModel.countryCode.value} - ${
                    Locale(
                        "",
                        viewModel.countryCode.value
                    ).displayName
                }",
                onClick = { openCurrencyDialog = true }
            )
            SettingsItem(
                icon = Icons.Outlined.CalendarToday,
                title = "Date Format",
                description = viewModel.dateFormat.value.format,
                onClick = { openDateFormatDialog = true }
            )
            SettingsItem(
                icon = Icons.Outlined.Category,
                title = "Transaction Categories",
                description = "${viewModel.txnCategoryCount} Categories",
                onClick = { navController.navigate(Screen.TxnCategoriesScreen.route) }
            )
            SettingsToggle(
                icon = Icons.AutoMirrored.Outlined.Label,
                title = "Bottom Nav Labels",
                checked = viewModel.bottomNavLabel.value,
                onClick = { viewModel.onEvent(SettingsEvent.UpdateBottomNavLabel(it)) }
            )
            SettingsItem(
                icon = Icons.Outlined.Info,
                title = "Check for Updates",
                description = "v${BuildConfig.VERSION_NAME}",
                onClick = { uriHandler.openUri("https://github.com/suyash01/credit-manager/releases") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            MultiChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                SegmentedButton(
                    checked = false,
                    onCheckedChange = {
                        if (!BackupWorker.isManualJobRunning(context)) {
                            try {
                                chooseBackupFile.launch(viewModel.getFilename())
                            } catch (e: ActivityNotFoundException) {
                                viewModel.onEvent(SettingsEvent.ShowSnackbar("File picker error"))
                            }
                        } else {
                            viewModel.onEvent(SettingsEvent.ShowSnackbar("Backup/Restore in progress"))
                        }
                    },
                    shape = SegmentedButtonDefaults.itemShape(0, 2)
                ) {
                    Text(text = "Create Backup")
                }
                SegmentedButton(
                    checked = false,
                    onCheckedChange = {
                        if (!BackupWorker.isManualJobRunning(context)) {
                            try {
                                chooseRestoreFile.launch(arrayOf("application/json"))
                            } catch (e: ActivityNotFoundException) {
                                viewModel.onEvent(SettingsEvent.ShowSnackbar("File picker error"))
                            }
                        } else {
                            viewModel.onEvent(SettingsEvent.ShowSnackbar("Backup/Restore in progress"))
                        }
                    },
                    shape = SegmentedButtonDefaults.itemShape(1, 2)
                ) {
                    Text(text = "Restore Backup")
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
                    shape = RoundedCornerShape(16.dp)
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