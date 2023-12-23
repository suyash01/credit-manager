package com.suyash.creditmanager.presentation.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suyash.creditmanager.data.settings.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStore: DataStore<AppSettings>
) : ViewModel() {

    private val _countryCode = mutableStateOf("IN")
    val countryCode: State<String> = _countryCode

    private val _countries = mutableStateOf(listOf(Locale("", "IN")))
    val countries: State<List<Locale>> = _countries

    init {
        viewModelScope.launch {
            dataStore.data.collect {
                _countryCode.value = it.countryCode
            }
        }

        viewModelScope.launch {
            val locales: MutableList<Locale> = mutableListOf()
            Locale.getISOCountries().forEach {
                locales.add(Locale("", it))
            }
            _countries.value = locales.sortedBy { it.displayName }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when(event) {
            is SettingsEvent.UpdateCurrency -> {
                _countryCode.value = event.countryCode
                viewModelScope.launch {
                    dataStore.updateData {
                        it.copy(countryCode = event.countryCode)
                    }
                }
            }
        }
    }
}