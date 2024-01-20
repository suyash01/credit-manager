package com.suyash.creditmanager.domain.util

sealed class CreditCardsOrder(val orderType: OrderType) {
    class Name(orderType: OrderType): CreditCardsOrder(orderType)
    class Expiry(orderType: OrderType): CreditCardsOrder(orderType)
    class Limit(orderType: OrderType): CreditCardsOrder(orderType)
    class DueDate(orderType: OrderType): CreditCardsOrder(orderType)
    class BillDate(orderType: OrderType): CreditCardsOrder(orderType)

    companion object {
        val defaultSorting: Map<String, Pair<CreditCardsOrder, CreditCardsOrder>> = mapOf(
            "Name" to Pair(Name(OrderType.Ascending), Name(OrderType.Descending)),
            "Expiry" to Pair(Expiry(OrderType.Ascending), Expiry(OrderType.Descending)),
            "Limit" to Pair(Limit(OrderType.Ascending), Limit(OrderType.Descending)),
            "Due Date" to Pair(DueDate(OrderType.Ascending), DueDate(OrderType.Descending)),
            "Bill Date" to Pair(BillDate(OrderType.Ascending), BillDate(OrderType.Descending))
        )
    }
}