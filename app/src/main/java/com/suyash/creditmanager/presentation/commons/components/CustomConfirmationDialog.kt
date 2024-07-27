package com.suyash.creditmanager.presentation.commons.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CustomConfirmationDialog(
    title: String,
    description: String,
    warningMessage: String? = null,
    infoMessage: String? = null,
    onDismissRequest: () -> Unit,
    onConfirmButton: () -> Unit,
    onDismissButton: () -> Unit,
) {
    AlertDialog(
        title = {
            Text(text = title)
        },
        text = {
            Column {
                Text(text = description, style = MaterialTheme.typography.bodyLarge)
                if(warningMessage != null) {
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Warning,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .padding(top = 2.dp)
                                .size(12.dp)
                        )
                        Text(
                            text = warningMessage,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                if(infoMessage != null) {
                    Row(modifier = Modifier.padding(top = 8.dp)) {
                        Icon(
                            imageVector = Icons.Outlined.Info,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(end = 4.dp)
                                .padding(top = 2.dp)
                                .size(12.dp)
                        )
                        Text(
                            text = infoMessage,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        },
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(
                onClick = onConfirmButton
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissButton
            ) {
                Text("Dismiss")
            }
        })
}

@Preview(showBackground = true)
@Composable
fun ConfirmationDialogPreview() {
    CustomConfirmationDialog(title = "Delete Credit Card?",
        description = "Do you want to delete XXX",
        warningMessage = "This will delete associated transaction(s) and emi(s)",
        onDismissRequest = { },
        onConfirmButton = { },
        onDismissButton = { })
}