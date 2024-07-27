package com.suyash.creditmanager.presentation.emis

import com.suyash.creditmanager.domain.model.EMI
import com.suyash.creditmanager.domain.util.order.EMIOrder

sealed class EMIsEvent {
    data class Order(val emiOrder: EMIOrder): EMIsEvent()
    data class SelectEMI(val emi: EMI): EMIsEvent()
    data class DeleteEMI(val emi: EMI?): EMIsEvent()
}