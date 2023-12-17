package com.suyash.creditmanager.presentation.credit_cards

import com.suyash.creditmanager.domain.util.CreditCardsOrder

sealed class CreditCardsEvent {
    data class Order(val creditCardsOrder: CreditCardsOrder): CreditCardsEvent()
    data object ToggleBottomSheet: CreditCardsEvent()
}