package com.suyash.creditmanager.presentation.add_edit_emi

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suyash.creditmanager.data.settings.AppSettings
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.model.EMI
import com.suyash.creditmanager.domain.use_case.CreditCardUseCases
import com.suyash.creditmanager.domain.use_case.EMIUseCases
import com.suyash.creditmanager.domain.util.CardType
import com.suyash.creditmanager.domain.util.CreditCardOrder
import com.suyash.creditmanager.domain.util.OrderType
import com.suyash.creditmanager.presentation.commons.TextInputState
import com.suyash.creditmanager.presentation.commons.validateInRange
import com.suyash.creditmanager.presentation.commons.validateMinMaxLength
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class AddEditEMIViewModel @Inject constructor(
    private val creditCardUseCases: CreditCardUseCases,
    private val emiUseCases: EMIUseCases,
    private val dataStore: DataStore<AppSettings>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var getCreditCardsJob: Job? = null

    private val _countryCode = mutableStateOf("IN")
    val countryCode: State<String> = _countryCode

    private val _dateFormatter = mutableStateOf(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val dateFormatter: State<DateTimeFormatter> = _dateFormatter

    private val _creditCards = mutableStateOf(emptyList<CreditCard>())
    val creditCards: State<List<CreditCard>> = _creditCards

    private val _name = mutableStateOf(TextInputState("", true, "Required"))
    val name: State<TextInputState<String>> = _name

    private val _emiAmount = mutableStateOf(TextInputState("", true, "Required"))
    val emiAmount: State<TextInputState<String>> = _emiAmount

    private val _interestRate = mutableStateOf(TextInputState("", true, "Required"))
    val interestRate: State<TextInputState<String>> = _interestRate

    private val _months = mutableStateOf(TextInputState("", true, "Required"))
    val months: State<TextInputState<String>> = _months

    private val _emiDate = mutableStateOf(LocalDate.now())
    val emiDate: State<LocalDate> = _emiDate

    private val _selectedCreditCard = mutableIntStateOf(-1)

    private val _taxRate = mutableStateOf(TextInputState(""))
    val taxRate: State<TextInputState<String>> = _taxRate

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
                        _name.value = TextInputState(it.name)
                        _emiAmount.value = TextInputState(it.name)
                        _emiAmount.value = TextInputState((it.amount * 100).toInt().toString())
                        _interestRate.value = TextInputState((it.rate * 100).toInt().toString())
                        _months.value = TextInputState(it.months.toString())
                        _emiDate.value = it.date
                        _selectedCreditCard.intValue = it.card ?: -1
                        _taxRate.value =
                            TextInputState(
                                it.taxRate?.let { tr -> tr * 100 }?.toInt()?.toString()
                                    ?: ""
                            )
                    }
                }
            }
        }
        viewModelScope.launch {
            dataStore.data.collect {
                _countryCode.value = it.countryCode
                _dateFormatter.value = it.dateFormat.formatter
            }
        }
    }

    fun onEvent(event: AddEditEMIEvent) {
        when (event) {
            is AddEditEMIEvent.EnteredName -> {
                _name.value = TextInputState(event.value).validateMinMaxLength(3, 25)
            }

            is AddEditEMIEvent.EnteredAmount -> {
                _emiAmount.value = TextInputState(event.value).validateInRange(1, 21474836, 2)
            }

            is AddEditEMIEvent.EnteredRate -> {
                _interestRate.value = TextInputState(event.value).validateInRange(1, 21474836, 2)
            }

            is AddEditEMIEvent.EnteredMonths -> {
                _interestRate.value = TextInputState(event.value).validateInRange(1, 2147483647)
            }

            is AddEditEMIEvent.EnteredStartDate -> {
                _emiDate.value = Instant.ofEpochMilli(
                    event.value?:
                    TimeUnit.DAYS.toMillis(LocalDate.now().toEpochDay())
                ).atZone(ZoneId.systemDefault()).toLocalDate()
            }

            is AddEditEMIEvent.SelectedCard -> {
                _selectedCreditCard.intValue = event.value
            }

            is AddEditEMIEvent.EnteredTaxRate -> {
                if (event.value.isEmpty()) {
                    _taxRate.value = TextInputState(event.value)
                } else {
                    _taxRate.value =
                        TextInputState(event.value).validateInRange(1, 21474836, 2)
                }

            }

            is AddEditEMIEvent.UpsertEMI -> {
                viewModelScope.launch {
                    if (!validateEMIData()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Please fix the errors"))
                        return@launch
                    }
                    emiUseCases.upsertEMI(
                        EMI(
                            id = currentEMIId.value,
                            name = name.value.data,
                            amount = (emiAmount.value.data.toInt() / 100F),
                            rate = (interestRate.value.data.toInt() / 100F),
                            months = months.value.data.toInt(),
                            date = emiDate.value,
                            card = if (_selectedCreditCard.intValue != -1) _selectedCreditCard.intValue else null,
                            taxRate = if (taxRate.value.data.isEmpty()) null else (taxRate.value.data.toInt() / 100F)
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
                        false,
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
            creditCards.value.find { it.id == _selectedCreditCard.intValue } ?: return ""
        return "${cc.cardName} (${cc.last4Digits})"
    }

    private fun validateEMIData(): Boolean {
        if (_name.value.error || _emiAmount.value.error || _interestRate.value.error || _months.value.error) {
            _name.value = _name.value.copy(displayError = true)
            _emiAmount.value = _emiAmount.value.copy(displayError = true)
            _interestRate.value = _interestRate.value.copy(displayError = true)
            _months.value = _months.value.copy(displayError = true)
            return false
        }
        return true
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object NavigateUp : UiEvent()
    }
}