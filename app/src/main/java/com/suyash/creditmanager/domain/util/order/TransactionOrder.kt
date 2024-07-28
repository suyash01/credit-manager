package com.suyash.creditmanager.domain.util.order

sealed class TransactionOrder(
    override var orderType: OrderType,
    override val label: String
) : Order {
    class Date(orderType: OrderType, label: String = "Date") : TransactionOrder(orderType, label)
    class Amount(orderType: OrderType, label: String = "Amount") :
        TransactionOrder(orderType, label)

    companion object {
        fun getOrderList(): List<TransactionOrder> {
            return listOf(
                Date(OrderType.Descending),
                Amount(OrderType.Ascending),
            )
        }
    }
}