package com.suyash.creditmanager.presentation.credit_cards

import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.util.CreditCardOrder
import com.suyash.creditmanager.domain.util.OrderType

data class CreditCardsState(
    val creditCards: List<CreditCard> = emptyList(),
    val creditCardsOrder: CreditCardOrder = CreditCardOrder.Name(OrderType.Ascending),
    val selectedCreditCard: CreditCard? = null,
    val countryCode: String = "IN",
    val isBottomSheetVisible: Boolean = false
)
