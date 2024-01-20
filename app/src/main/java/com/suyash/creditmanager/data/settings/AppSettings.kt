package com.suyash.creditmanager.data.settings

import com.suyash.creditmanager.domain.util.DateFormat
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val countryCode: String = "IN",
    val dateFormat: DateFormat = DateFormat.DDMMYYYY
)