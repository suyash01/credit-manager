package com.suyash.creditmanager.domain.util.order

sealed interface Order {

    var orderType: OrderType
    val label: String
}