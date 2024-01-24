package com.suyash.creditmanager.domain.use_case.emi

import com.suyash.creditmanager.domain.model.EMI
import com.suyash.creditmanager.domain.repository.EMIRepository

class DeleteEMI(private val repository: EMIRepository) {
    suspend operator fun invoke(emi: EMI) {
        return repository.deleteEMI(emi)
    }
}