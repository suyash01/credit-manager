package com.suyash.creditmanager.domain.use_case.emi

import com.suyash.creditmanager.domain.model.EMI
import com.suyash.creditmanager.domain.repository.EMIRepository
import kotlinx.coroutines.flow.Flow

class GetEMI(private val repository: EMIRepository) {
    operator fun invoke(id: Int): Flow<EMI?> {
        return repository.getEMI(id)
    }
}