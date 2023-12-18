package com.suyash.creditmanager.presentation.credit_cards

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.suyash.creditmanager.domain.use_case.CreditCardUseCases
import com.suyash.creditmanager.domain.util.CreditCardsOrder
import com.suyash.creditmanager.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreditCardsViewModel @Inject constructor(
    private val creditCardUseCases: CreditCardUseCases
): ViewModel() {
    
    private val _state = mutableStateOf(CreditCardsState())
    val state: State<CreditCardsState> = _state

    private var getCreditCardsJob: Job? = null

    init {
        getCreditCards(CreditCardsOrder.Name(OrderType.Ascending))
    }

    fun onEvent(event: CreditCardsEvent) {
        when(event) {
            is CreditCardsEvent.Order -> {
                if(state.value.creditCardsOrder::class == event.creditCardsOrder::class &&
                    state.value.creditCardsOrder.orderType == event.creditCardsOrder.orderType) {
                    return
                }
                getCreditCards(event.creditCardsOrder)
            }
            is CreditCardsEvent.ToggleBottomSheet -> {
                _state.value = state.value.copy(
                    selectedCreditCard = event.creditCard,
                    isBottomSheetVisible = !state.value.isBottomSheetVisible
                )
            }
            is CreditCardsEvent.DeleteCreditCard -> {
                if(event.creditCard != null) {
                    viewModelScope.launch {
                        creditCardUseCases.deleteCreditCard(event.creditCard)
                    }
                }
            }
        }
    }

    private fun getCreditCards(creditCardsOrder: CreditCardsOrder) {
        getCreditCardsJob?.cancel()
        getCreditCardsJob = creditCardUseCases.getCreditCards(creditCardsOrder).onEach { creditCards ->
            _state.value = state.value.copy(
                creditCards = creditCards,
                creditCardsOrder = creditCardsOrder
            )
        }.launchIn(viewModelScope)
    }
}