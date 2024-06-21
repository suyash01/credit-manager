package com.suyash.creditmanager.presentation.add_edit_emi

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.model.EMI
import com.suyash.creditmanager.domain.use_case.CreditCardUseCases
import com.suyash.creditmanager.domain.use_case.EMIUseCases
import com.suyash.creditmanager.domain.util.CardType
import com.suyash.creditmanager.domain.util.CreditCardOrder
import com.suyash.creditmanager.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddEditEMIViewModel @Inject constructor(
    private val creditCardUseCases: CreditCardUseCases,
    private val emiUseCases: EMIUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var getCreditCardsJob: Job? = null

    private val _dateFormatter = mutableStateOf(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val dateFormatter: State<DateTimeFormatter> = _dateFormatter

    private val _creditCards = mutableStateOf(emptyList<CreditCard>())
    val creditCards: State<List<CreditCard>> = _creditCards

    private val _selectedCreditCard = mutableIntStateOf(-1)
    private val selectedCreditCard: State<Int> = _selectedCreditCard

    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _emiAmount = mutableStateOf("")
    val emiAmount: State<String> = _emiAmount

    private val _interestRate = mutableStateOf("")
    val interestRate: State<String> = _interestRate

    private val _taxRate = mutableStateOf("")
    val taxRate: State<String> = _taxRate

    private val _months = mutableStateOf("")
    val months: State<String> = _months

    private val _emiDate = mutableStateOf(LocalDate.now())
    val emiDate: State<LocalDate> = _emiDate

    private val _currentEMIId = mutableIntStateOf(0)
    val currentEMIId: State<Int> = _currentEMIId

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getCreditCards(CreditCardOrder.Name(OrderType.Ascending))
        savedStateHandle.get<Int>("emiId")?.let { emiId ->
            if (emiId != -1) {
                viewModelScope.launch {
                    emiUseCases.getEMI(emiId).first()?.let {
                        _currentEMIId.intValue = it.id
                        _name.value = it.name
                        _emiAmount.value = it.name
                        _emiAmount.value = it.amount.toString()
                        _interestRate.value = it.rate.toString()
                        _taxRate.value = it.taxRate?.toString() ?: ""
                        _months.value = it.months.toString()
                        _emiDate.value = it.date
                        _selectedCreditCard.intValue = it.card ?: -1
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditEMIEvent) {
        when (event) {
            is AddEditEMIEvent.EnteredName -> {
                _name.value = event.value
            }

            is AddEditEMIEvent.EnteredAmount -> {
                if (event.value.isEmpty()) {
                    _emiAmount.value = event.value
                }
                val parts = event.value.split(".")
                if (event.value.toFloatOrNull() != null
                    && event.value.toFloat() > 0
                    && parts.size <= 2
                    && parts.getOrElse(1) { "0" }.length <= 2
                ) {
                    _emiAmount.value = event.value
                }
            }

            is AddEditEMIEvent.EnteredRate -> {
                if (event.value.isEmpty()) {
                    _interestRate.value = event.value
                }
                val parts = event.value.split(".")
                if (event.value.toFloatOrNull() != null
                    && event.value.toFloat() > 0
                    && parts.size <= 2
                    && parts.getOrElse(1) { "0" }.length <= 2
                ) {
                    _interestRate.value = event.value
                }
            }

            is AddEditEMIEvent.EnteredTaxRate -> {
                if (event.value.isEmpty()) {
                    _interestRate.value = event.value
                }
                val parts = event.value.split(".")
                if (event.value.toFloatOrNull() != null
                    && event.value.toFloat() > 0
                    && parts.size <= 2
                    && parts.getOrElse(1) { "0" }.length <= 2
                ) {
                    _taxRate.value = event.value
                }
            }

            is AddEditEMIEvent.EnteredMonths -> {
                if (event.value.isEmpty()) {
                    _months.value = event.value
                }
                if (event.value.toIntOrNull() != null && event.value.toInt() > 0) {
                    _months.value = event.value
                }
            }

            is AddEditEMIEvent.EnteredStartDate -> {
                _emiDate.value = event.value
            }

            is AddEditEMIEvent.SelectedCard -> {
                _selectedCreditCard.intValue = event.value.id
            }

            is AddEditEMIEvent.UpsertEMI -> {
                viewModelScope.launch {
                    emiUseCases.upsertEMI(
                        EMI(
                            id = currentEMIId.value,
                            name = name.value,
                            amount = emiAmount.value.toFloatOrNull() ?: 0.0F,
                            rate = interestRate.value.toFloatOrNull() ?: 0.0F,
                            months = months.value.toIntOrNull() ?: 0,
                            date = emiDate.value,
                            taxRate = taxRate.value.toFloatOrNull(),
                            card = if (_selectedCreditCard.intValue != -1) _selectedCreditCard.intValue else null
                        )
                    )
                    _eventFlow.emit(UiEvent.NavigateUp)
                }
            }

            is AddEditEMIEvent.BackPressed -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateUp)
                }
            }
        }
    }

    private fun getCreditCards(creditCardsOrder: CreditCardOrder) {
        getCreditCardsJob?.cancel()
        getCreditCardsJob =
            creditCardUseCases.getCreditCards(creditCardsOrder).onEach { creditCards ->
                _creditCards.value = listOf(
                    CreditCard(
                        "Select Credit Card",
                        "XXXX",
                        "",
                        0,
                        0,
                        CardType.VISA,
                        0,
                        "",
                        -1
                    )
                ) + creditCards
            }.launchIn(viewModelScope)
    }

    fun getCCDisplay(): String {
        val cc: CreditCard =
            creditCards.value.find { it.id == selectedCreditCard.value } ?: return ""
        return "${cc.cardName} (${cc.last4Digits})"
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object NavigateUp : UiEvent()
    }
}