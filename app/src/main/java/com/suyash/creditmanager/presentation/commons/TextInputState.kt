package com.suyash.creditmanager.presentation.commons

data class TextInputState<T>(
    val data: T,
    val error: Boolean = false,
    val errorMessage: String? = null,
    val displayError: Boolean = false
)
