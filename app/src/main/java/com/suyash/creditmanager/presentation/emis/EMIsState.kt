package com.suyash.creditmanager.presentation.emis

import androidx.compose.runtime.Immutable
import com.suyash.creditmanager.domain.model.EMI
import com.suyash.creditmanager.domain.util.DateFormat
import com.suyash.creditmanager.domain.util.order.EMIOrder
import com.suyash.creditmanager.domain.util.order.OrderType

@Immutable
data class EMIsState(
    val emis: List<EMI> = emptyList(),
    val emiOrder: EMIOrder = EMIOrder.Name(OrderType.Ascending),
    val countryCode: String = "IN",
    val dateFormat: DateFormat = DateFormat.DDMMYYYY,
    val selectedEMI: EMI? = null
)
