package com.suyash.creditmanager.presentation.transactions

import com.suyash.creditmanager.domain.model.Transaction
import com.suyash.creditmanager.domain.util.order.TransactionOrder

sealed class TransactionsEvent {
    data class Order(val transactionOrder: TransactionOrder): TransactionsEvent()
    data class ToggleBottomSheet(val transaction: Transaction): TransactionsEvent()
    data class DeleteTransaction(val transaction: Transaction?): TransactionsEvent()
}