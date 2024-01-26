package com.suyash.creditmanager.domain.util

sealed class CreditCardOrder(val orderType: OrderType) {
    class Name(orderType: OrderType): CreditCardOrder(orderType)
    class Expiry(orderType: OrderType): CreditCardOrder(orderType)
    class Limit(orderType: OrderType): CreditCardOrder(orderType)
    class DueDate(orderType: OrderType): CreditCardOrder(orderType)
    class BillDate(orderType: OrderType): CreditCardOrder(orderType)

    companion object {
        val displayNameMap: Map<String, String?> = mapOf(
            "Name" to Name::class.simpleName,
            "Expiry" to Expiry::class.simpleName,
            "Limit" to Limit::class.simpleName,
            "Due Date" to DueDate::class.simpleName,
            "Bill Date" to BillDate::class.simpleName
        )

        val sorting: Map<String?, Pair<CreditCardOrder, CreditCardOrder>> = mapOf(
            Name::class.simpleName to Pair(Name(OrderType.Ascending), Name(OrderType.Descending)),
            Expiry::class.simpleName to Pair(Expiry(OrderType.Ascending), Expiry(OrderType.Descending)),
            Limit::class.simpleName to Pair(Limit(OrderType.Ascending), Limit(OrderType.Descending)),
            DueDate::class.simpleName to Pair(DueDate(OrderType.Ascending), DueDate(OrderType.Descending)),
            BillDate::class.simpleName to Pair(BillDate(OrderType.Ascending), BillDate(OrderType.Descending))
        )
    }
}