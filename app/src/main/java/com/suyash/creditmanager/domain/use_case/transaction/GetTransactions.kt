package com.suyash.creditmanager.domain.use_case.transaction

import com.suyash.creditmanager.domain.model.Transaction
import com.suyash.creditmanager.domain.repository.TransactionRepository
import com.suyash.creditmanager.domain.util.order.OrderType
import com.suyash.creditmanager.domain.util.order.TransactionOrder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetTransactions(
    private val repository: TransactionRepository
) {
    operator fun invoke(transactionOrder: TransactionOrder = TransactionOrder.Date(OrderType.Descending)): Flow<List<Transaction>> {
        return repository.getTransactions().map { transactions ->
            when(transactionOrder.orderType) {
                is OrderType.Ascending -> {
                    when(transactionOrder) {
                        is TransactionOrder.Date -> transactions.sortedBy { it.date }
                        is TransactionOrder.Amount -> transactions.sortedBy { it.amount }
                    }
                }
                is OrderType.Descending -> {
                    when(transactionOrder) {
                        is TransactionOrder.Date -> transactions.sortedByDescending { it.date }
                        is TransactionOrder.Amount -> transactions.sortedByDescending { it.amount }
                    }
                }
            }
        }
    }
}