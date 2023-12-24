package com.suyash.creditmanager.data.repository

import com.suyash.creditmanager.data.source.dao.TransactionDao
import com.suyash.creditmanager.domain.model.Transaction
import com.suyash.creditmanager.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow

class TransactionRepositoryImpl(private val dao: TransactionDao): TransactionRepository {
    override suspend fun upsertTransaction(transaction: Transaction) {
        dao.upsertTransaction(transaction)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        dao.deleteTransaction(transaction)
    }

    override fun getTransactions(): Flow<List<Transaction>> {
        return dao.getTransactions()
    }

    override suspend fun getTransaction(id: Int): Transaction? {
        return dao.getTransaction(id)
    }
}