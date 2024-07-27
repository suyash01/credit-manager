package com.suyash.creditmanager.domain.util.order

sealed class OrderType {
    data object Ascending: OrderType()
    data object Descending: OrderType()

    fun getReverse(): OrderType {
        return when (this) {
            is Ascending -> Descending
            is Descending -> Ascending
        }
    }
}