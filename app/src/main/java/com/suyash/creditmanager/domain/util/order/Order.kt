package com.suyash.creditmanager.domain.util.order

sealed interface Order {

    val orderType: OrderType
    val label: String
}