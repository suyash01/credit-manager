package com.suyash.creditmanager.domain.use_case.credit_card

import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.repository.CreditCardRepository
import com.suyash.creditmanager.domain.util.CreditCardsOrder
import com.suyash.creditmanager.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCreditCards(
    private val repository: CreditCardRepository
) {
    operator fun invoke(creditCardOrder: CreditCardsOrder = CreditCardsOrder.Name(OrderType.Ascending)): Flow<List<CreditCard>> {
        return repository.getCreditCards().map { creditCards ->
            when(creditCardOrder.orderType) {
                is OrderType.Ascending -> {
                    when(creditCardOrder) {
                        is CreditCardsOrder.Name -> creditCards.sortedBy { it.cardName.lowercase() }
                        is CreditCardsOrder.Expiry -> creditCards.sortedBy { it.expiryDate }
                        is CreditCardsOrder.Limit -> creditCards.sortedBy { it.limit }
                        is CreditCardsOrder.DueDate -> creditCards.sortedBy { it.dueDate }
                        is CreditCardsOrder.BillDate -> creditCards.sortedBy { it.billDate }
                    }
                }
                is OrderType.Descending -> {
                    when(creditCardOrder) {
                        is CreditCardsOrder.Name -> creditCards.sortedByDescending { it.cardName.lowercase() }
                        is CreditCardsOrder.Expiry -> creditCards.sortedByDescending { it.expiryDate }
                        is CreditCardsOrder.Limit -> creditCards.sortedByDescending { it.limit }
                        is CreditCardsOrder.DueDate -> creditCards.sortedByDescending { it.dueDate }
                        is CreditCardsOrder.BillDate -> creditCards.sortedByDescending { it.billDate }
                    }
                }
            }
        }
    }
}