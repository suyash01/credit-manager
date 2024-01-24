package com.suyash.creditmanager.presentation.credit_cards

import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.util.CreditCardOrder

sealed class CreditCardsEvent {
    data class Order(val creditCardsOrder: CreditCardOrder): CreditCardsEvent()
    data class ToggleBottomSheet(val creditCard: CreditCard): CreditCardsEvent()
    data class DeleteCreditCard(val creditCard: CreditCard?): CreditCardsEvent()
}