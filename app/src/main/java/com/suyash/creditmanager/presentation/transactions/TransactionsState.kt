package com.suyash.creditmanager.presentation.transactions

import com.suyash.creditmanager.domain.model.Transaction
import com.suyash.creditmanager.domain.util.OrderType
import com.suyash.creditmanager.domain.util.TransactionOrder

data class TransactionsState(
    val transactions: List<Transaction> = emptyList(),
    val transactionOrder: TransactionOrder = TransactionOrder.Date(OrderType.Ascending),
    val selectedTransaction: Transaction? = null,
    val countryCode: String = "IN",
    val dateFormat: String = "dd/MM/yyyy",
    val isBottomSheetVisible: Boolean = false
)
