package com.suyash.creditmanager.presentation.emi_detail

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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    var totalAmount: Float = 0.0F
    var schedule: List<Payment> = emptyList()

    private var getEMIDataJob: Job? = null

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        viewModelScope.launch {
            dataStore.data.collect {
                countryCode = it.countryCode
                dateFormat = it.dateFormat
            }
        }
        savedStateHandle.get<Int>("emiId")?.let {
            getEMIData(it)
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

    private fun getEMIData(id: Int) {
        getEMIDataJob?.cancel()
        getEMIDataJob = emiUseCases.getEMI(id).onEach { emi ->
            this.emi = emi
            emi?.let {
                generateAmortizationSchedule(
                    emi.amount.toDouble(),
                    emi.rate.toDouble(),
                    emi.months,
                    emi.taxRate?.toDouble()?:0.0)
            }
            if (emi?.card == null) creditCard = null
            emi?.card?.let {
                creditCardUseCases.getCreditCard(it).first()?.let { cc ->
                    creditCard = cc
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun generateAmortizationSchedule(loanAmount: Double, annualInterestRate: Double, loanTermInMonths: Int, taxRate: Double) {
        val monthlyInterestRate = annualInterestRate / 12 / 100
        val monthlyPayment = loanAmount *
                (monthlyInterestRate * (1 + monthlyInterestRate).pow(loanTermInMonths.toDouble())) /
                ((1 + monthlyInterestRate).pow(loanTermInMonths.toDouble()) - 1)

        var remainingBalance = loanAmount
        val payments = mutableListOf<Payment>()

        for (i in 1..loanTermInMonths) {
            val interestPayment = remainingBalance * monthlyInterestRate
            val taxOnInterest = interestPayment * (taxRate / 100)
            val principalPayment = monthlyPayment - interestPayment
            remainingBalance -= principalPayment

            val payment = Payment(i, monthlyPayment, principalPayment, interestPayment, remainingBalance, taxOnInterest)
            payments.add(payment)
        }

        emiAmount = monthlyPayment.toFloat()
        totalAmount = (monthlyPayment * loanTermInMonths).toFloat()
        schedule = payments
    }

    data class Payment(
        val paymentNumber: Int,
        val paymentAmount: Double,
        val principalAmount: Double,
        val interestAmount: Double,
        val remainingBalance: Double,
        val taxOnInterest: Double
    )

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data object NavigateUp: UiEvent()
    }
}