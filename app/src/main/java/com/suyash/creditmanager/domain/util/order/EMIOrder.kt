package com.suyash.creditmanager.domain.util.order

sealed class EMIOrder(override val orderType: OrderType, override val label: String) : Order {
    class Name(orderType: OrderType, label: String = "Name") : EMIOrder(orderType, label)
    class Amount(orderType: OrderType, label: String = "Amount") : EMIOrder(orderType, label)
    class Rate(orderType: OrderType, label: String = "Rate") : EMIOrder(orderType, label)
    class Months(orderType: OrderType, label: String = "Months") : EMIOrder(orderType, label)
    class Date(orderType: OrderType, label: String = "Date") : EMIOrder(orderType, label)
    class EMIsPaid(orderType: OrderType, label: String = "EMIs Paid") : EMIOrder(orderType, label)
    class EMIsRemaining(orderType: OrderType, label: String = "EMIs Remaining") : EMIOrder(orderType, label)

    companion object {
        fun getOrderList(): List<EMIOrder> {
            return listOf(
                Name(OrderType.Ascending),
                Amount(OrderType.Ascending),
                Rate(OrderType.Ascending),
                Months(OrderType.Ascending),
                Date(OrderType.Descending),
                EMIsPaid(OrderType.Ascending),
                EMIsRemaining(OrderType.Descending)
            )
        }
    }
}