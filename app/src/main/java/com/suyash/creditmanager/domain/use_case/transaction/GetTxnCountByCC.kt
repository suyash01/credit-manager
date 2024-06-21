package com.suyash.creditmanager.domain.use_case.transaction

import com.suyash.creditmanager.domain.repository.TransactionRepository

class GetTxnCountByCC(
    private val repository: TransactionRepository
) {

    suspend operator fun invoke(id: Int): Int {
        return repository.getTxnCountByCC(id)
    }
}
