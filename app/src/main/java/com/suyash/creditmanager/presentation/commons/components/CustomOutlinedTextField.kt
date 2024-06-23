package com.suyash.creditmanager.presentation.commons.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActionScope
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
import com.suyash.creditmanager.presentation.commons.TextInputState

@Composable
fun CustomOutlinedTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: TextInputState<String>,
    onValueChange: (String) -> Unit,
    prefix: @Composable (() -> Unit)? = null,
    placeholder: String = "",
    visualTransformation: VisualTransformation = VisualTransformation.None,
    imeAction: ImeAction = ImeAction.Next,
    keyboardType: KeyboardType = KeyboardType.Text,
    keyboardActionOnNext: (KeyboardActionScope.() -> Unit)? = null
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value.data,
        singleLine = true,
        onValueChange = onValueChange,
        isError = value.error && value.displayError,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        prefix = prefix,
        trailingIcon = {
            if (value.error && value.displayError)
                Icon(Icons.Filled.Error,"error", tint = MaterialTheme.colorScheme.error)
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
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction, keyboardType = keyboardType),
        keyboardActions = KeyboardActions(onNext = keyboardActionOnNext)
    )
}