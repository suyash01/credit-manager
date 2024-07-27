package com.suyash.creditmanager.presentation.credit_cards

import androidx.compose.runtime.Immutable
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.util.order.CreditCardOrder
import com.suyash.creditmanager.domain.util.order.OrderType

@Immutable
data class CreditCardsState(
    val creditCards: List<CreditCard> = emptyList(),
    val creditCardsOrder: CreditCardOrder = CreditCardOrder.Name(OrderType.Ascending),
    val selectedCreditCard: CreditCard? = null,
    val countryCode: String = "IN",
    val isBottomSheetVisible: Boolean = false
)
