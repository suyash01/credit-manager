package com.suyash.creditmanager.presentation.emis

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suyash.creditmanager.data.settings.AppSettings
import com.suyash.creditmanager.domain.use_case.EMIUseCases
import com.suyash.creditmanager.domain.util.order.EMIOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EMIsViewModel @Inject constructor(
    private val emiUseCases: EMIUseCases,
    private val dataStore: DataStore<AppSettings>
): ViewModel() {

    private val _state = mutableStateOf(EMIsState())
    val state: State<EMIsState> = _state

    private var getEMIsJob: Job? = null

    init {
        getEMIs(state.value.emiOrder)
        viewModelScope.launch {
            dataStore.data.collect {
                _state.value = state.value.copy(
                    countryCode = it.countryCode,
                    dateFormat = it.dateFormat
                )
            }
        }
    }

    fun onEvent(event: EMIsEvent) {
        when(event) {
            is EMIsEvent.SelectEMI -> {
                _state.value = state.value.copy(
                    selectedEMI = event.emi
                )
            }
            is EMIsEvent.DeleteEMI -> {
                event.emi?.let {
                    viewModelScope.launch {
                        emiUseCases.deleteEMI(event.emi)
                    }
                }
            }
            is EMIsEvent.Order -> {
                if(state.value.emiOrder::class == event.emiOrder::class &&
                    state.value.emiOrder.orderType == event.emiOrder.orderType) {
                    return
                }
                getEMIs(event.emiOrder)
            }
        }
    }

    private fun getEMIs(emiOrder: EMIOrder) {
        getEMIsJob?.cancel()
        getEMIsJob = emiUseCases.getEMIs(emiOrder).onEach {
            _state.value = _state.value.copy(
                emis = it,
                emiOrder = emiOrder
            )
        }.launchIn(viewModelScope)
    }
}