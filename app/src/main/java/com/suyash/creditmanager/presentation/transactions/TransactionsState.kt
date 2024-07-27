package com.suyash.creditmanager.presentation.transactions

import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.model.Transaction
import com.suyash.creditmanager.domain.util.DateFormat
import com.suyash.creditmanager.domain.util.order.OrderType
import com.suyash.creditmanager.domain.util.order.TransactionOrder

data class TransactionsState(
    val creditCards: Map<Int, CreditCard> = emptyMap(),
    val transactions: List<Transaction> = emptyList(),
    val transactionOrder: TransactionOrder = TransactionOrder.Date(OrderType.Ascending),
    val selectedTransaction: Transaction? = null,
    val countryCode: String = "IN",
    val dateFormat: DateFormat = DateFormat.DDMMYYYY,
    val isBottomSheetVisible: Boolean = false
)
