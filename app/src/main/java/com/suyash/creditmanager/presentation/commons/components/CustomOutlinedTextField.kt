package com.suyash.creditmanager.presentation.commons.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import com.suyash.creditmanager.presentation.commons.TextInputState

@Composable
fun CustomOutlinedTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: TextInputState<String>,
    onValueChange: (String) -> Unit,
    prefix: String = "",
    suffix: String = "",
    placeholder: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value.data,
        singleLine = true,
        onValueChange = onValueChange,
        isError = value.error && value.displayError,
        label = { Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        placeholder = { Text(text = placeholder, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        prefix = { Text(text = prefix) },
        suffix = { Text(text = suffix) },
        trailingIcon = {
            if (value.error && value.displayError)
                Icon(Icons.Filled.Error, "error", tint = MaterialTheme.colorScheme.error)
        },
        supportingText = {
            if (value.error && value.displayError && value.errorMessage != null) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = value.errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = imeAction,
            keyboardType = keyboardType
        ),
        keyboardActions = keyboardActions
    )
}