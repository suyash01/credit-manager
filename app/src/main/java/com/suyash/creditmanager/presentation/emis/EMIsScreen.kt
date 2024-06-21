package com.suyash.creditmanager.presentation.emis

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.suyash.creditmanager.presentation.emis.component.EMIItem
import com.suyash.creditmanager.presentation.commons.Screen
import com.suyash.creditmanager.presentation.commons.components.ConfirmationDialog

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
fun EMIsScreen(
    navController: NavController,
    viewModel: EMIsViewModel = hiltViewModel()
) {
    val haptics = LocalHapticFeedback.current
    val bottomSheetState = rememberModalBottomSheetState()
    var isItemBottomSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }
    var openDeleteConfirmationDialog by rememberSaveable {
        mutableStateOf(false)
    }

    var fabHeight by remember {
        mutableIntStateOf(0)
    }
    val fabHeightInDp = with(LocalDensity.current) { fabHeight.toDp() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "EMIs") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.onGloballyPositioned { fabHeight = it.size.height },
                onClick = { navController.navigate(Screen.AddEditEMIScreen.route) }
            ) {
                Icon(Icons.Filled.Add, "Add EMI")
            }
        }
    ) {
        paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues),
            contentPadding = PaddingValues(
                bottom = fabHeightInDp + 16.dp
            )
        ) {
            items(viewModel.state.value.emis) { emi ->
                EMIItem(
                    emi = emi,
                    countryCode = viewModel.state.value.countryCode,
                    dateFormat = viewModel.state.value.dateFormat,
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {
                                navController.navigate(
                                    Screen.EMIDetailScreen.route + "?emiId=" + emi.id
                                )
                            },
                            onLongClick = {
                                haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.onEvent(EMIsEvent.SelectEMI(emi))
                                isItemBottomSheetOpen = true
                            }
                        )
                )
            }
        }
        if(openDeleteConfirmationDialog) {
            ConfirmationDialog(
                title = "Delete EMI?",
                description = "Do you want to delete ${viewModel.state.value.selectedEMI?.name}",
                onDismissRequest = { openDeleteConfirmationDialog = false },
                onConfirmButton = {
                    openDeleteConfirmationDialog = false
                    viewModel.onEvent(EMIsEvent.DeleteEMI(viewModel.state.value.selectedEMI))
                },
                onDismissButton = { openDeleteConfirmationDialog = false }
            )
        }
        if(isItemBottomSheetOpen) {
            ModalBottomSheet(
                sheetState = bottomSheetState,
                onDismissRequest = { isItemBottomSheetOpen = false }
            ) {
                Column(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .clickable {
                                isItemBottomSheetOpen = false
                                navController.navigate(
                                    Screen.AddEditEMIScreen.route +
                                            "?emiId=${viewModel.state.value.selectedEMI?.id}"
                                )
                            }
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Icon(Icons.Filled.Edit, "Edit EMI")
                        Text(text = "Edit", modifier = Modifier.padding(start = 16.dp))
                    }
                    Row(
                        modifier = Modifier
                            .clickable {
                                isItemBottomSheetOpen = false
                                openDeleteConfirmationDialog = true
                            }
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Icon(Icons.Filled.Delete, "Delete EMI")
                        Text(text = "Delete", modifier = Modifier.padding(start = 16.dp))
                    }
                }
            }
        }
    }
}