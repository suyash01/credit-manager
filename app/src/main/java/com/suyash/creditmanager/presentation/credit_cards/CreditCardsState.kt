package com.suyash.creditmanager.presentation.credit_cards

import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.util.CreditCardsOrder
import com.suyash.creditmanager.domain.util.OrderType

data class CreditCardsState(
    val creditCards: List<CreditCard> = emptyList(),
    val creditCardsOrder: CreditCardsOrder = CreditCardsOrder.Name(OrderType.Ascending),
    val selectedCreditCard: CreditCard? = null,
    val countryCode: String = "IN",
    val isBottomSheetVisible: Boolean = false
)
