package com.suyash.creditmanager.domain.repository

import com.suyash.creditmanager.domain.model.TxnCategory
import kotlinx.coroutines.flow.Flow

interface TxnCategoryRepository {
    suspend fun upsertTxnCategory(txnCategory: TxnCategory)

    suspend fun deleteTxnCategory(txnCategory: TxnCategory)

    fun getTxnCategories(): Flow<List<TxnCategory>>

    suspend fun getTxnCategory(id: Int): TxnCategory?
}