package com.suyash.creditmanager.presentation.credit_cards

import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.util.CreditCardsOrder

sealed class CreditCardsEvent {
    data class Order(val creditCardsOrder: CreditCardsOrder): CreditCardsEvent()
    data class ToggleBottomSheet(val creditCard: CreditCard): CreditCardsEvent()
    data class DeleteCreditCard(val creditCard: CreditCard?): CreditCardsEvent()
}