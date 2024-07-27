package com.suyash.creditmanager.domain.util.order

sealed class TransactionOrder(val orderType: OrderType) {
    class Date(orderType: OrderType): TransactionOrder(orderType)
    class Amount(orderType: OrderType): TransactionOrder(orderType)
}