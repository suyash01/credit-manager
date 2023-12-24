package com.suyash.creditmanager.data.source.dao

import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.suyash.creditmanager.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionDao {

    @Upsert
    suspend fun upsertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions")
    fun getTransactions(): Flow<List<Transaction>>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransaction(id: Int): Transaction?
}
