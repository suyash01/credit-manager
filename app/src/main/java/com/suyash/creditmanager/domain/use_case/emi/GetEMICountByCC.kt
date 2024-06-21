package com.suyash.creditmanager.domain.use_case.emi

import com.suyash.creditmanager.domain.repository.EMIRepository

class GetEMICountByCC(private val repository: EMIRepository) {
    suspend operator fun invoke(id: Int): Int {
        return repository.getEMICountByCC(id)
    }
}