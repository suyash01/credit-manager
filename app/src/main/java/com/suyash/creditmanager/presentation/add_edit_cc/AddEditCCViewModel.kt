package com.suyash.creditmanager.presentation.add_edit_cc

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suyash.creditmanager.data.settings.AppSettings
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.use_case.CreditCardUseCases
import com.suyash.creditmanager.domain.util.CardType
import com.suyash.creditmanager.presentation.commons.TextInputState
import com.suyash.creditmanager.presentation.commons.validateCCExpiry
import com.suyash.creditmanager.presentation.commons.validateInRange
import com.suyash.creditmanager.presentation.commons.validateIsNumeric
import com.suyash.creditmanager.presentation.commons.validateMinMaxLength
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditCCViewModel @Inject constructor(
    private val creditCardUseCases: CreditCardUseCases,
    private val dataStore: DataStore<AppSettings>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _countryCode = mutableStateOf("IN")
    val countryCode: State<String> = _countryCode

    private val _cardType = mutableStateOf(CardType.VISA)
    val cardType: State<CardType> = _cardType

    private val _cardName = mutableStateOf(TextInputState("", true, "Required"))
    val cardName: State<TextInputState<String>> = _cardName

    private val _last4Digits = mutableStateOf(TextInputState("", true, "Required"))
    val last4Digits: State<TextInputState<String>> = _last4Digits

    private val _expiry = mutableStateOf(TextInputState("", true, "Required"))
    val expiry: State<TextInputState<String>> = _expiry

    private val _gracePeriod = mutableStateOf(false)
    val gracePeriod: State<Boolean> = _gracePeriod

    private val _billDate = mutableStateOf(TextInputState("", true, "Required"))
    val billDate: State<TextInputState<String>> = _billDate

    private val _dueDate = mutableStateOf(TextInputState("", true, "Required"))
    val dueDate: State<TextInputState<String>> = _dueDate

    private val _limit = mutableStateOf(TextInputState("", true, "Required"))
    val limit: State<TextInputState<String>> = _limit

    private val _bankName = mutableStateOf(TextInputState(""))
    val bankName: State<TextInputState<String>> = _bankName

    private val _currentCCId = mutableIntStateOf(0)
    val currentCCId: State<Int> = _currentCCId

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        savedStateHandle.get<Int>("ccId")?.let { ccId ->
            if (ccId != -1) {
                viewModelScope.launch {
                    creditCardUseCases.getCreditCard(ccId).first()?.let {
                        _currentCCId.intValue = it.id
                        _cardType.value = it.cardType
                        _cardName.value = TextInputState(it.cardName)
                        _last4Digits.value = TextInputState(it.last4Digits)
                        _expiry.value = TextInputState(it.expiryDate)
                        _billDate.value = TextInputState(it.billDate.toString())
                        _gracePeriod.value = it.gracePeriod
                        _dueDate.value = TextInputState(it.dueDate.toString())
                        _limit.value = TextInputState(it.limit.toString())
                        _bankName.value = TextInputState(it.bankName ?: "")
                    }
                }
            }
        }
        viewModelScope.launch {
            dataStore.data.collect {
                _countryCode.value = it.countryCode
            }
        }
    }

    fun onEvent(event: AddEditCCEvent) {
        when (event) {
            is AddEditCCEvent.SelectedCardType -> {
                _cardType.value = CardType.valueOf(event.value)
            }

            is AddEditCCEvent.EnteredCardName -> {
                _cardName.value = TextInputState(event.value.trim()).validateMinMaxLength(3, 25)
            }

            is AddEditCCEvent.EnteredLast4Digits -> {
                _last4Digits.value = TextInputState(event.value.trim())
                    .validateIsNumeric()
                    .validateMinMaxLength(4, 4)
            }

            is AddEditCCEvent.EnteredExpiry -> {
                if (event.value.length <= 4) {
                    _expiry.value = TextInputState(event.value.trim()).validateCCExpiry()
                }
            }

            is AddEditCCEvent.CheckedGracePeriod -> {
                _gracePeriod.value = event.value
            }

            is AddEditCCEvent.EnteredBillDate -> {
                _billDate.value = TextInputState(event.value.trim()).validateInRange(1, 31)
            }

            is AddEditCCEvent.EnteredDueDate -> {
                if (_gracePeriod.value) {
                    _dueDate.value = TextInputState(event.value.trim()).validateIsNumeric()
                } else {
                    _dueDate.value = TextInputState(event.value.trim()).validateInRange(1, 31)
                }
            }

            is AddEditCCEvent.EnteredLimit -> {
                _limit.value = TextInputState(event.value.trim()).validateInRange(1, 2147483647)
            }

            is AddEditCCEvent.EnteredBankName -> {
                _bankName.value = TextInputState(event.value.trim()).validateMinMaxLength(0, 25)
            }

            is AddEditCCEvent.UpsertCreditCard -> {
                viewModelScope.launch {
                    if(!validCCData()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Please fix the errors"))
                        return@launch
                    }
                    creditCardUseCases.upsertCreditCard(
                        CreditCard(
                            id = currentCCId.value,
                            cardType = cardType.value,
                            cardName = cardName.value.data,
                            last4Digits = last4Digits.value.data,
                            expiryDate = expiry.value.data,
                            billDate = billDate.value.data.toInt(),
                            gracePeriod = gracePeriod.value,
                            dueDate = dueDate.value.data.toInt(),
                            limit = limit.value.data.toInt(),
                            bankName = bankName.value.data
                        )
                    )
                    _eventFlow.emit(UiEvent.NavigateUp)
                }
            }

            is AddEditCCEvent.BackPressed -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateUp)
                }
            }
        }
    }

    private fun validCCData(): Boolean {
        if (_cardName.value.error || _last4Digits.value.error || _expiry.value.error ||
                _billDate.value.error || _dueDate.value.error ||  _limit.value.error) {
            _cardName.value = _cardName.value.copy(displayError = true)
            _last4Digits.value = _last4Digits.value.copy(displayError = true)
            _expiry.value = _expiry.value.copy(displayError = true)
            _billDate.value = _billDate.value.copy(displayError = true)
            _dueDate.value = _dueDate.value.copy(displayError = true)
            _limit.value = _limit.value.copy(displayError = true)
            return false
        }
        return true
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object NavigateUp : UiEvent()
    }
}
