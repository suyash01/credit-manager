package com.suyash.creditmanager.domain.use_case.emi

import com.suyash.creditmanager.domain.model.EMI
import com.suyash.creditmanager.domain.repository.EMIRepository
import com.suyash.creditmanager.domain.util.EMIOrder
import com.suyash.creditmanager.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetEMIs(private val repository: EMIRepository) {
    operator fun invoke(emiOrder: EMIOrder = EMIOrder.Date(OrderType.Descending)): Flow<List<EMI>> {
        return repository.getEMIs().map { emis ->
            when(emiOrder.orderType) {
                is OrderType.Ascending -> {
                    when(emiOrder) {
                        is EMIOrder.Name -> emis.sortedBy { it.name.lowercase() }
                        is EMIOrder.Amount -> emis.sortedBy { it.amount }
                        is EMIOrder.Rate -> emis.sortedBy { it.rate }
                        is EMIOrder.Months -> emis.sortedBy { it.months }
                        is EMIOrder.Date -> emis.sortedBy { it.date }
                    }
                }
                is OrderType.Descending -> {
                    when(emiOrder) {
                        is EMIOrder.Name -> emis.sortedByDescending { it.name.lowercase() }
                        is EMIOrder.Amount -> emis.sortedByDescending { it.amount }
                        is EMIOrder.Rate -> emis.sortedByDescending { it.rate }
                        is EMIOrder.Months -> emis.sortedByDescending { it.months }
                        is EMIOrder.Date -> emis.sortedByDescending { it.date }
                    }
                }
            }
        }
    }
}