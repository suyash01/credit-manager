package com.suyash.creditmanager.presentation.add_edit_txn

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suyash.creditmanager.data.settings.AppSettings
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.model.Transaction
import com.suyash.creditmanager.domain.model.TxnCategory
import com.suyash.creditmanager.domain.use_case.CreditCardUseCases
import com.suyash.creditmanager.domain.use_case.TransactionUseCases
import com.suyash.creditmanager.domain.use_case.TxnCategoryUseCases
import com.suyash.creditmanager.domain.util.order.CreditCardOrder
import com.suyash.creditmanager.domain.util.order.OrderType
import com.suyash.creditmanager.domain.util.TransactionType
import com.suyash.creditmanager.presentation.commons.TextInputState
import com.suyash.creditmanager.presentation.commons.validateInRange
import com.suyash.creditmanager.presentation.commons.validateMinMaxLength
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
class AddEditTxnViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCases,
    private val creditCardUseCases: CreditCardUseCases,
    private val txnCategoryUseCase: TxnCategoryUseCases,
    private val dataStore: DataStore<AppSettings>,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var getCreditCardsJob: Job? = null
    private var getTxnCategoriesJob: Job? = null

    private val _countryCode = mutableStateOf("IN")
    val countryCode: State<String> = _countryCode

    private val _creditCards = mutableStateOf(emptyList<CreditCard>())
    val creditCards: State<List<CreditCard>> = _creditCards

    private val _txnCategories = mutableStateOf(emptyList<TxnCategory>())
    val txnCategories: State<List<TxnCategory>> = _txnCategories

    private val _dateFormatter = mutableStateOf(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val dateFormatter: State<DateTimeFormatter> = _dateFormatter

    private val _name = mutableStateOf(TextInputState("", true, "Required"))
    val name: State<TextInputState<String>> = _name

    private val _selectedCreditCard = mutableIntStateOf(-1)

    private val _txnType = mutableStateOf(TransactionType.DEBIT)
    val txnType: State<TransactionType> = _txnType

    private val _txnCategory = mutableStateOf("")
    val txnCategory: State<String> = _txnCategory

    private val _txnDate = mutableStateOf(LocalDate.now())
    val txnDate: State<LocalDate> = _txnDate

    private val _txnAmount = mutableStateOf(TextInputState("", true, "Required"))
    val txnAmount: State<TextInputState<String>> = _txnAmount

    private val _currentTxnId = mutableIntStateOf(0)
    val currentTxnId: State<Int> = _currentTxnId

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getCreditCardsAndCategories(CreditCardOrder.Name(OrderType.Ascending))
        savedStateHandle.get<Int>("txnId")?.let { txnId ->
            if (txnId != -1) {
                viewModelScope.launch {
                    transactionUseCase.getTransaction(txnId)?.also { transaction ->
                        _currentTxnId.intValue = transaction.id
                        _name.value = TextInputState(transaction.name)
                        _selectedCreditCard.intValue = transaction.card
                        _txnType.value = transaction.type
                        _txnCategory.value = transaction.category ?: ""
                        _txnDate.value = transaction.date
                        _txnAmount.value =
                            TextInputState((transaction.amount * 100).toInt().toString())
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

    fun onEvent(event: AddEditTxnEvent) {
        when (event) {
            is AddEditTxnEvent.EnteredName -> {
                _name.value = TextInputState(event.value).validateMinMaxLength(3, 25)
            }

            is AddEditTxnEvent.SelectedCard -> {
                _selectedCreditCard.intValue = event.value
            }

            is AddEditTxnEvent.SelectedTxnType -> {
                _txnType.value = event.value
                _txnCategory.value = ""
            }

            is AddEditTxnEvent.SelectedTxnCategory -> {
                _txnCategory.value = event.value
            }

            is AddEditTxnEvent.EnteredDate -> {
                _txnDate.value = Instant.ofEpochMilli(
                    event.value?:
                    TimeUnit.DAYS.toMillis(LocalDate.now().toEpochDay())
                ).atZone(ZoneId.systemDefault()).toLocalDate()
            }

            is AddEditTxnEvent.EnteredAmount -> {
                _txnAmount.value =
                    TextInputState(event.value.trim()).validateInRange(1, 21474836, 2)
            }

            is AddEditTxnEvent.UpsertTransaction -> {
                viewModelScope.launch {
                    if (_selectedCreditCard.intValue == -1) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Please select a credit card"))
                        return@launch
                    }
                    if (!validateTxnData()) {
                        _eventFlow.emit(UiEvent.ShowSnackbar("Please fix the errors"))
                        return@launch
                    }
                    transactionUseCase.upsertTransaction(
                        Transaction(
                            id = currentTxnId.value,
                            name = name.value.data,
                            card = _selectedCreditCard.intValue,
                            type = txnType.value,
                            category = txnCategory.value,
                            date = txnDate.value,
                            amount = (txnAmount.value.data.toInt() / 100.0F)
                        )
                    )
                    _eventFlow.emit(UiEvent.NavigateUp)
                }
            }

            is AddEditTxnEvent.BackPressed -> {
                viewModelScope.launch {
                    _eventFlow.emit(UiEvent.NavigateUp)
                }
            }
        }
    }

    private fun getCreditCardsAndCategories(creditCardsOrder: CreditCardOrder) {
        getCreditCardsJob?.cancel()
        getCreditCardsJob =
            creditCardUseCases.getCreditCards(creditCardsOrder).onEach { creditCards ->
                _creditCards.value = creditCards
            }.launchIn(viewModelScope)
        getTxnCategoriesJob?.cancel()
        getTxnCategoriesJob = txnCategoryUseCase.getTxnCategories().onEach { txnCategories ->
            _txnCategories.value = txnCategories
        }.launchIn(viewModelScope)
    }

    fun getCCDisplay(): String {
        val cc: CreditCard =
            creditCards.value.find { it.id == _selectedCreditCard.intValue } ?: return ""
        return "${cc.cardName} (${cc.last4Digits})"
    }

    private fun validateTxnData(): Boolean {
        if (_name.value.error || _txnAmount.value.error) {
            _name.value = _name.value.copy(displayError = true)
            _txnAmount.value = _txnAmount.value.copy(displayError = true)
            return false
        }
        return true
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        data object NavigateUp : UiEvent()
    }
}
