package com.suyash.creditmanager.presentation.settings

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suyash.creditmanager.data.settings.AppSettings
import com.suyash.creditmanager.domain.util.DateFormat
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

    private val _dateFormat = mutableStateOf(DateFormat.DDMMYYYY)
    val dateFormat: State<DateFormat> = _dateFormat

    private val _bottomNavLabel = mutableStateOf(true)
    val bottomNavLabel: State<Boolean> = _bottomNavLabel

    private val _countries = mutableStateOf(listOf(Locale("", "IN")))
    val countries: State<List<Locale>> = _countries

    init {
        viewModelScope.launch {
            dataStore.data.collect {
                _countryCode.value = it.countryCode
                _dateFormat.value = it.dateFormat
                _bottomNavLabel.value = it.bottomNavLabel
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
            is SettingsEvent.UpdateDateFormat -> {
                _dateFormat.value = event.dateFormat
                viewModelScope.launch {
                    dataStore.updateData {
                        it.copy(dateFormat = event.dateFormat)
                    }
                }
            }
            is SettingsEvent.UpdateBottomNavLabel -> {
                _bottomNavLabel.value = event.shown
                viewModelScope.launch {
                    dataStore.updateData {
                        it.copy(bottomNavLabel = event.shown)
                    }
                }
            }
        }
    }
}