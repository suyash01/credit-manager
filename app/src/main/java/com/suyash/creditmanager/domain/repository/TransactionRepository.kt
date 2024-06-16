package com.suyash.creditmanager.domain.repository

import com.suyash.creditmanager.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    suspend fun upsertTransaction(transaction: Transaction)

    suspend fun deleteTransaction(transaction: Transaction)

    fun getTransactions(): Flow<List<Transaction>>

    fun getTransactionsByCC(id: Int): Flow<List<Transaction>>

    suspend fun getTransaction(id: Int): Transaction?
}