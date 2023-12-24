package com.suyash.creditmanager.domain.util

sealed class TransactionOrder(val orderType: OrderType) {
    class Date(orderType: OrderType): TransactionOrder(orderType)
    class Amount(orderType: OrderType): TransactionOrder(orderType)
}