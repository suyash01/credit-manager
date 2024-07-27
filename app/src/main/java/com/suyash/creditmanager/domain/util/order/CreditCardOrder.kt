package com.suyash.creditmanager.domain.util.order

sealed class CreditCardOrder(
    override val orderType: OrderType,
    override val label: String
) : Order {
    class Name(orderType: OrderType, label: String = "Name") : CreditCardOrder(orderType, label)
    class Expiry(orderType: OrderType, label: String = "Expiry") : CreditCardOrder(orderType, label)
    class Limit(orderType: OrderType, label: String = "Limit") : CreditCardOrder(orderType, label)
    class DueDate(orderType: OrderType, label: String = "Due Date") :
        CreditCardOrder(orderType, label)

    class BillDate(orderType: OrderType, label: String = "Bill Date") :
        CreditCardOrder(orderType, label)

    companion object {
        fun getOrderList(): List<CreditCardOrder> {
            return listOf(
                Name(OrderType.Ascending),
                Expiry(OrderType.Ascending),
                Limit(OrderType.Ascending),
                DueDate(OrderType.Ascending),
                BillDate(OrderType.Ascending)
            )
        }
    }
}