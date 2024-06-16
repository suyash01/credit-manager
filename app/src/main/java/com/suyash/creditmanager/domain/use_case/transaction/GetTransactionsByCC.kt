package com.suyash.creditmanager.domain.use_case.transaction

import com.suyash.creditmanager.domain.model.Transaction
import com.suyash.creditmanager.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class GetTransactionsByCC(
    private val repository: TransactionRepository
) {
    operator fun invoke(id: Int): Flow<List<Transaction>> {
        return repository.getTransactionsByCC(id)
    }
}
