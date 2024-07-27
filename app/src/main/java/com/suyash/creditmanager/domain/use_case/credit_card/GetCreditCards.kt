package com.suyash.creditmanager.domain.use_case.credit_card

import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.repository.CreditCardRepository
import com.suyash.creditmanager.domain.util.order.CreditCardOrder
import com.suyash.creditmanager.domain.util.order.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetCreditCards(
    private val repository: CreditCardRepository
) {
    operator fun invoke(creditCardOrder: CreditCardOrder = CreditCardOrder.Name(OrderType.Ascending)): Flow<List<CreditCard>> {
        return repository.getCreditCards().map { creditCards ->
            when(creditCardOrder.orderType) {
                is OrderType.Ascending -> {
                    when(creditCardOrder) {
                        is CreditCardOrder.Name -> creditCards.sortedBy { it.cardName.lowercase() }
                        is CreditCardOrder.Expiry -> creditCards.sortedBy { it.expiryDate }
                        is CreditCardOrder.Limit -> creditCards.sortedBy { it.limit }
                        is CreditCardOrder.DueDate -> creditCards.sortedBy { it.dueDate }
                        is CreditCardOrder.BillDate -> creditCards.sortedBy { it.billDate }
                    }
                }
                is OrderType.Descending -> {
                    when(creditCardOrder) {
                        is CreditCardOrder.Name -> creditCards.sortedByDescending { it.cardName.lowercase() }
                        is CreditCardOrder.Expiry -> creditCards.sortedByDescending { it.expiryDate }
                        is CreditCardOrder.Limit -> creditCards.sortedByDescending { it.limit }
                        is CreditCardOrder.DueDate -> creditCards.sortedByDescending { it.dueDate }
                        is CreditCardOrder.BillDate -> creditCards.sortedByDescending { it.billDate }
                    }
                }
            }
        }
    }
}