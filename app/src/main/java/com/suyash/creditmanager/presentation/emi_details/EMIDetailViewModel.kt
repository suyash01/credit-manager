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
    var schedule: List<Payment> = emptyList()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("emiId")?.let {
            viewModelScope.launch {
                emiUseCases.getEMI(it)?.also { e ->
                    emi = e
                    generateAmortizationSchedule(e.amount.toDouble(), e.rate.toDouble(), e.months)
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

    private fun generateAmortizationSchedule(loanAmount: Double, annualInterestRate: Double, loanTermInMonths: Int) {
        val monthlyInterestRate = annualInterestRate / 12 / 100
        val monthlyPayment = loanAmount *
                (monthlyInterestRate * (1 + monthlyInterestRate).pow(loanTermInMonths.toDouble())) /
                ((1 + monthlyInterestRate).pow(loanTermInMonths.toDouble()) - 1)

        var remainingBalance = loanAmount
        val payments = mutableListOf<Payment>()

        for (i in 1..loanTermInMonths) {
            val interestPayment = remainingBalance * monthlyInterestRate
            val principalPayment = monthlyPayment - interestPayment
            remainingBalance -= principalPayment

            val payment = Payment(i, monthlyPayment, principalPayment, interestPayment, remainingBalance)
            payments.add(payment)
        }

        emiAmount = monthlyPayment.toFloat()
        schedule = payments
    }

    data class Payment(
        val paymentNumber: Int,
        val paymentAmount: Double,
        val principalAmount: Double,
        val interestAmount: Double,
        val remainingBalance: Double
    )

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data object NavigateUp: UiEvent()
    }
}