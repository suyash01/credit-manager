package com.suyash.creditmanager.domain.util

sealed class CreditCardsOrder(val orderType: OrderType) {
    class Name(orderType: OrderType): CreditCardsOrder(orderType)
    class Expiry(orderType: OrderType): CreditCardsOrder(orderType)
    class Limit(orderType: OrderType): CreditCardsOrder(orderType)
    class DueDate(orderType: OrderType): CreditCardsOrder(orderType)
    class BillDate(orderType: OrderType): CreditCardsOrder(orderType)
}