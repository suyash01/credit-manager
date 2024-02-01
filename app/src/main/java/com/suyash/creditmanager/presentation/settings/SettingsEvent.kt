package com.suyash.creditmanager.presentation.settings

import com.suyash.creditmanager.domain.util.DateFormat

sealed class SettingsEvent {

    data class UpdateCurrency(val countryCode: String): SettingsEvent()
    data class UpdateDateFormat(val dateFormat: DateFormat): SettingsEvent()
    data class UpdateBottomNavLabel(val shown: Boolean): SettingsEvent()
}