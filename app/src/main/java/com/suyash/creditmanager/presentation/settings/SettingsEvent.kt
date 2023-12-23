package com.suyash.creditmanager.presentation.settings

sealed class SettingsEvent {

    data class UpdateCurrency(val countryCode: String): SettingsEvent()
}