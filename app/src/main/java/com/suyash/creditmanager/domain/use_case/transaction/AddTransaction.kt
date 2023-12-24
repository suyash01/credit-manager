package com.suyash.creditmanager.domain.use_case.transaction

import com.suyash.creditmanager.domain.model.Transaction
import com.suyash.creditmanager.domain.repository.TransactionRepository

class AddTransaction(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.upsertTransaction(transaction)
    }
}