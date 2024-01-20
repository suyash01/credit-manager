package com.suyash.creditmanager.presentation.transactions

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suyash.creditmanager.data.settings.AppSettings
import com.suyash.creditmanager.domain.use_case.CreditCardUseCases
import com.suyash.creditmanager.domain.use_case.TransactionUseCase
import com.suyash.creditmanager.domain.util.CreditCardsOrder
import com.suyash.creditmanager.domain.util.OrderType
import com.suyash.creditmanager.domain.util.TransactionOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val creditCardUseCases: CreditCardUseCases,
    private val transactionUseCase: TransactionUseCase,
    private val dataStore: DataStore<AppSettings>
): ViewModel() {

    private val _state = mutableStateOf(TransactionsState())
    val state: State<TransactionsState> = _state

    private var getCreditCardsJob: Job? = null
    private var getTransactionsJob: Job? = null

    init {
        getCreditCards(CreditCardsOrder.Name(OrderType.Ascending))
        getTransactions(TransactionOrder.Date(OrderType.Descending))
        viewModelScope.launch {
            dataStore.data.collect {
                _state.value = state.value.copy(
                    countryCode = it.countryCode,
                    dateFormat = it.dateFormat
                )
            }
        }
    }

    fun onEvent(event: TransactionsEvent) {
        when(event) {
            is TransactionsEvent.Order -> {
                if(state.value.transactionOrder::class == event.transactionOrder::class &&
                    state.value.transactionOrder.orderType == event.transactionOrder.orderType) {
                    return
                }
                getTransactions(event.transactionOrder)
            }
            is TransactionsEvent.ToggleBottomSheet -> {
                _state.value = state.value.copy(
                    selectedTransaction = event.transaction,
                    isBottomSheetVisible = !state.value.isBottomSheetVisible
                )
            }
            is TransactionsEvent.DeleteTransaction -> {
                if(event.transaction != null) {
                    viewModelScope.launch {
                        transactionUseCase.deleteTransaction(event.transaction)
                    }
                }
            }
        }
    }

    private fun getCreditCards(creditCardsOrder: CreditCardsOrder) {
        getCreditCardsJob?.cancel()
        getCreditCardsJob = creditCardUseCases.getCreditCards(creditCardsOrder).onEach { creditCards ->
            _state.value = state.value.copy(
                creditCards = creditCards.associateBy { it.id }
            )
        }.launchIn(viewModelScope)
    }

    private fun getTransactions(transactionOrder: TransactionOrder) {
        getTransactionsJob?.cancel()
        getTransactionsJob = transactionUseCase.getTransactions(transactionOrder).onEach { transactions ->
            _state.value = state.value.copy(
                transactions = transactions,
                transactionOrder = transactionOrder
            )
        }.launchIn(viewModelScope)
    }
}