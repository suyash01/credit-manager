package com.suyash.creditmanager.presentation.add_edit_txn

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.model.Transaction
import com.suyash.creditmanager.domain.use_case.CreditCardUseCases
import com.suyash.creditmanager.domain.use_case.TransactionUseCase
import com.suyash.creditmanager.domain.util.CreditCardsOrder
import com.suyash.creditmanager.domain.util.OrderType
import com.suyash.creditmanager.domain.util.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AddEditTxnViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCase,
    private val creditCardUseCases: CreditCardUseCases,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private var getCreditCardsJob: Job? = null

    private val _creditCards = mutableStateOf(emptyList<CreditCard>())
    val creditCards: State<List<CreditCard>> = _creditCards

    private val _dateFormatter = mutableStateOf(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    val dateFormatter: State<DateTimeFormatter> = _dateFormatter

    private val _selectedCreditCard = mutableIntStateOf(-1)
    val selectedCreditCard: State<Int> = _selectedCreditCard

    private val _txnType = mutableStateOf(TransactionType.DEBIT)
    val txnType: State<TransactionType> = _txnType

    private val _txnDate = mutableStateOf(LocalDate.now())
    val txnDate: State<LocalDate> = _txnDate

    private val _txnAmount = mutableStateOf("")
    val txnAmount: State<String> = _txnAmount

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    var currentTxnId: Int? = null

    init {
        getCreditCards(CreditCardsOrder.Name(OrderType.Ascending))
        savedStateHandle.get<Int>("txnId")?.let { txnId ->
            if(txnId != -1) {
                viewModelScope.launch {
                    transactionUseCase.getTransaction(txnId)?.also { transaction ->
                        currentTxnId = transaction.id
                        _txnType.value = transaction.type
                        _txnAmount.value = transaction.amount.toString()
                        _selectedCreditCard.intValue = transaction.card
                        _txnDate.value = transaction.date
                    }
                }
            }
        }
    }

    fun onEvent(event: AddEditTxnEvent) {
        when (event) {
            is AddEditTxnEvent.EnteredAmount -> {
                _txnAmount.value = event.value
            }
            is AddEditTxnEvent.EnteredDate -> {
                _txnDate.value = event.value
            }
            is AddEditTxnEvent.SelectedCard -> {
                _selectedCreditCard.intValue = event.value.id!!
            }
            is AddEditTxnEvent.SelectedTxnType -> {
                _txnType.value = event.value
            }
            is AddEditTxnEvent.UpsertTransaction -> {
                viewModelScope.launch {
                    transactionUseCase.upsertTransaction(
                        Transaction(
                            type = txnType.value,
                            amount = txnAmount.value.toFloatOrNull() ?: 0.0F,
                            card = selectedCreditCard.value,
                            date = txnDate.value,
                            id = currentTxnId
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

    private fun getCreditCards(creditCardsOrder: CreditCardsOrder) {
        getCreditCardsJob?.cancel()
        getCreditCardsJob = creditCardUseCases.getCreditCards(creditCardsOrder).onEach { creditCards ->
            _creditCards.value = creditCards
        }.launchIn(viewModelScope)
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        data object NavigateUp: UiEvent()
    }
}
