package com.suyash.creditmanager.presentation.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suyash.creditmanager.data.settings.AppSettings
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreditManagerViewModel @Inject constructor(
    private val dataStore: DataStore<AppSettings>
): ViewModel() {
    private val _bottomNavLabel = mutableStateOf(true)
    val bottomNavLabel: State<Boolean> = _bottomNavLabel

    init {
        viewModelScope.launch {
            dataStore.data.collect {
                _bottomNavLabel.value = it.bottomNavLabel
            }
        }
    }
}