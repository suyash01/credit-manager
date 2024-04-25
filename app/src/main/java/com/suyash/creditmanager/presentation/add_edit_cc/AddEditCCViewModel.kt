package com.suyash.creditmanager.presentation.add_edit_cc

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.model.InvalidCreditCardException
import com.suyash.creditmanager.domain.use_case.CreditCardUseCases
import com.suyash.creditmanager.domain.util.CardType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditCCViewModel @Inject constructor(
    private val creditCardUseCases: CreditCardUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {

    private val _last4Digits = mutableStateOf("")
    val last4Digits: State<String> = _last4Digits

    private val _cardName = mutableStateOf("")
    val cardName: State<String> = _cardName

    private val _expiry = mutableStateOf("")
    val expiry: State<String> = _expiry

    private val _billDate = mutableStateOf("")
    val billDate: State<String> = _billDate

    private val _dueDate = mutableStateOf("")
    val dueDate: State<String> = _dueDate

    private val _limit = mutableStateOf("")
    val limit: State<String> = _limit

    private val _bankName = mutableStateOf("")
    val bankName: State<String> = _bankName

    private val _cardType = mutableStateOf(CardType.VISA)
    val cardType: State<CardType> = _cardType

    private val _currentCCId = mutableIntStateOf(0)
    val currentCCId: State<Int> = _currentCCId

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("ccId")?.let { ccId ->
            if(ccId != -1) {
                viewModelScope.launch {
                    creditCardUseCases.getCreditCard(ccId)?.also { creditCard ->
                        _currentCCId.intValue = creditCard.id
                        _last4Digits.value = creditCard.last4Digits
                        _cardName.value = creditCard.cardName
                        _expiry.value = creditCard.expiryDate
                        _billDate.value = creditCard.billDate.toString()
                        _dueDate.value = creditCard.dueDate.toString()
                        _limit.value = creditCard.limit.toString()
                        _bankName.value = creditCard.bankName?:""
                        _cardType.value = creditCard.cardType
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditCCEvent) {
        when(event) {
            is AddEditCCEvent.SelectedCardType -> {
                _cardType.value = event.value
            }
            is AddEditCCEvent.EnteredLast4Digits -> {
                if(event.value.isEmpty()) {
                    _last4Digits.value = event.value
                }
                if(event.value.length <= 4 && event.value.toIntOrNull() != null && event.value.toInt() >= 0) {
                    _last4Digits.value = event.value
                }
            }
            is AddEditCCEvent.EnteredCardName -> {
                _cardName.value = event.value
            }
            is AddEditCCEvent.EnteredExpiry -> {
                if(event.value.isEmpty()) {
                    _expiry.value = event.value
                }
                if(validateExpiry(event.value)) {
                    _expiry.value = event.value
                }
            }
            is AddEditCCEvent.EnteredDueDate -> {
                if(event.value.isEmpty()) {
                    _dueDate.value = event.value
                }
                val value: Int? = event.value.toIntOrNull()
                if(value != null && event.value.toInt() > 0 && event.value.toInt() < 31) {
                    _dueDate.value = event.value
                }
            }
            is AddEditCCEvent.EnteredBillDate -> {
                if(event.value.isEmpty()) {
                    _billDate.value = event.value
                }
                val value: Int? = event.value.toIntOrNull()
                if(value != null && event.value.toInt() > 0 && value < 31) {
                    _billDate.value = event.value
                }
            }
            is AddEditCCEvent.EnteredLimit -> {
                if(event.value.isEmpty()) {
                    _limit.value = event.value
                }
                if(event.value.toIntOrNull() != null && event.value.toInt() > 0) {
                    _limit.value = event.value
                }
            }
            is AddEditCCEvent.EnteredBankName -> {
                _bankName.value = event.value
            }
            is AddEditCCEvent.UpsertCreditCard -> {
                viewModelScope.launch {
                    try {
                        creditCardUseCases.upsertCreditCard(
                            CreditCard(
                                cardName = cardName.value,
                                last4Digits = last4Digits.value,
                                expiryDate = expiry.value,
                                billDate = billDate.value.toIntOrNull()?:0,
                                dueDate = dueDate.value.toIntOrNull()?:0,
                                cardType = cardType.value,
                                limit = limit.value.toIntOrNull()?:0,
                                bankName = bankName.value,
                                id = currentCCId.value
                            )
                        )
                        _eventFlow.emit(UiEvent.NavigateUp)
                    } catch (e: InvalidCreditCardException) {
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save credit card"
                            )
                        )
                    }
                }
            }
            is AddEditCCEvent.BackPressed -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateUp)
                }
            }
        }
    }

    private fun validateExpiry(expiry: String): Boolean {
        if(expiry.toIntOrNull() == null) return false
        return when (expiry.length) {
            1 -> expiry.toInt() < 2
            2 -> expiry.toInt() in 1..12
            3 -> expiry.toInt() < 130
            4 -> expiry.toInt() in 101.. 1299
            else -> false
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data object NavigateUp: UiEvent()
    }
}
