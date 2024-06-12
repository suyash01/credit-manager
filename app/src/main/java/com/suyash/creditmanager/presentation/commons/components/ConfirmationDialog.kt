package com.suyash.creditmanager.presentation.commons.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ConfirmationDialog(
    icon: ImageVector,
    title: String,
    description: String,
    onDismissRequest: () -> Unit,
    onConfirmButton: () -> Unit,
    onDismissButton: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(icon, description)
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(text = description)
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
    ConfirmationDialog(icon = Icons.Filled.Delete,
        title = "Delete Credit Card?",
        description = "Do you want to delete XXX",
        onDismissRequest = { },
        onConfirmButton = { },
        onDismissButton = { })
}