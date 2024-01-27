package com.suyash.creditmanager.presentation.emi_details

import androidx.datastore.core.DataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suyash.creditmanager.data.settings.AppSettings
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.model.EMI
import com.suyash.creditmanager.domain.use_case.CreditCardUseCases
import com.suyash.creditmanager.domain.use_case.EMIUseCases
import com.suyash.creditmanager.domain.util.DateFormat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.pow

@HiltViewModel
class EMIDetailViewModel @Inject constructor(
    private val creditCardUseCases: CreditCardUseCases,
    private val emiUseCases: EMIUseCases,
    private val dataStore: DataStore<AppSettings>,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    var emi: EMI? = null
    var creditCard: CreditCard? = null
    var countryCode: String = "IN"
    var dateFormat: DateFormat = DateFormat.DDMMYYYY
    var emiAmount: Float = 0.0F
    var schedule: List<EMISchedule> = emptyList()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("emiId")?.let {
            viewModelScope.launch {
                emiUseCases.getEMI(it)?.also { e ->
                    emi = e
                    calculateEMISchedule(e.amount, e.rate, e.months)
                }
                emi?.card?.let {
                    creditCardUseCases.getCreditCard(it)?.also { cc ->
                        creditCard = cc
                    }
                }
                dataStore.data.collect {
                    countryCode = it.countryCode
                    dateFormat = it.dateFormat
                }
            }
        }
    }

    fun onEvent(event: EMIDetailEvent) {
        when (event) {
            is EMIDetailEvent.BackPressed -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateUp)
                }
            }
        }
    }

    private fun calculateEMISchedule(startingAmount: Float, interestRate: Float, tenure: Int) {
        val schedule: MutableList<EMISchedule> = mutableListOf()
        var principal: Float = startingAmount
        val monthlyRate: Float = interestRate/1200
        val emi: Float = startingAmount*monthlyRate*(1+monthlyRate).pow(tenure)/((1+monthlyRate).pow(tenure)-1)
        for (i in 1..tenure) {
            val interest = principal*(monthlyRate)
            val principalPaid: Float = emi - interest
            principal -= principalPaid
            schedule.add(EMISchedule(principalPaid, interest, principal))
        }
        this.emiAmount = emi
        this.schedule = schedule.toList()
    }

    data class EMISchedule(
        val principal: Float,
        val interest: Float,
        val amount: Float
    )

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data object NavigateUp: UiEvent()
    }
}