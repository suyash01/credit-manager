package com.suyash.creditmanager.presentation.commons.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.suyash.creditmanager.presentation.commons.model.ItemAction

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomActionBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    actions: List<ItemAction>
) {
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        sheetState = bottomSheetState,
        onDismissRequest = onDismissRequest
    ) {
        Column(
            modifier = modifier.padding(bottom = 16.dp)
        ) {
            actions.forEach {
                Row(
                    modifier = Modifier
                        .clickable {
                            onDismissRequest()
                            it.onClick()
                        }
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Icon(it.icon, it.iconName)
                    Text(text = it.title, modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}