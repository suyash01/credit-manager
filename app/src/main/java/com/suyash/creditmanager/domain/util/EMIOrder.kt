package com.suyash.creditmanager.domain.util

sealed class EMIOrder(val orderType: OrderType) {
    class Name(orderType: OrderType): EMIOrder(orderType)
    class Amount(orderType: OrderType): EMIOrder(orderType)
    class Rate(orderType: OrderType): EMIOrder(orderType)
    class Months(orderType: OrderType): EMIOrder(orderType)
    class Date(orderType: OrderType): EMIOrder(orderType)
}