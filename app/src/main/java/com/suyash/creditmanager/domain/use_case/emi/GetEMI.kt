package com.suyash.creditmanager.domain.use_case.emi

import com.suyash.creditmanager.domain.model.EMI
import com.suyash.creditmanager.domain.repository.EMIRepository

class GetEMI(private val repository: EMIRepository) {
    suspend operator fun invoke(id: Int): EMI? {
        return repository.getEMI(id)
    }
}