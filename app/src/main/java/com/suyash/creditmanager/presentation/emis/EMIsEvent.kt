package com.suyash.creditmanager.presentation.emis

import com.suyash.creditmanager.domain.model.EMI

sealed class EMIsEvent {
    data class SelectEMI(val emi: EMI): EMIsEvent()
    data class DeleteEMI(val emi: EMI?): EMIsEvent()
}