package com.suyash.creditmanager.domain.util

sealed class OrderType {
    object Ascending: OrderType()
    object Descending: OrderType()
}