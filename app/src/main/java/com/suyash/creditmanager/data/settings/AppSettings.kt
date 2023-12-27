package com.suyash.creditmanager.data.settings

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val countryCode: String = "IN",
    val dateFormat: String = "dd/MM/yyyy"
)