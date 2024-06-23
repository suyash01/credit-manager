package com.suyash.creditmanager.presentation.cc_detail

import androidx.datastore.core.DataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suyash.creditmanager.data.settings.AppSettings
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.use_case.CreditCardUseCases
import com.suyash.creditmanager.domain.use_case.EMIUseCases
import com.suyash.creditmanager.domain.use_case.TransactionUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CCDetailViewModel @Inject constructor(
    private val creditCardUseCases: CreditCardUseCases,
    private val transactionUseCases: TransactionUseCases,
    private val emiUseCases: EMIUseCases,
    private val dataStore: DataStore<AppSettings>,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    var creditCard: CreditCard? = null

    private var getCCDataJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("ccId")?.let {
            getCreditCardData(it)
        }
    }

    fun onEvent(event: CCDetailEvent) {
        when (event) {
            is CCDetailEvent.BackPressed -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateUp)
                }
            }
        }
    }

    private fun getCreditCardData(it: Int) {
        getCCDataJob?.cancel()
        getCCDataJob = creditCardUseCases.getCreditCard(it).onEach { creditCard ->
            this.creditCard = creditCard
        }.launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data object NavigateUp: UiEvent()
    }
}