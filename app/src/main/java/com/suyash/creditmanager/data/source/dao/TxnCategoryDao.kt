package com.suyash.creditmanager.data.source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.suyash.creditmanager.domain.model.TxnCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TxnCategoryDao {

    @Upsert
    suspend fun upsertTxnCategory(txnCategory: TxnCategory)

    @Delete
    suspend fun deleteTxnCategory(txnCategory: TxnCategory)

    @Query("SELECT * FROM txn_categories")
    fun getTxnCategories(): Flow<List<TxnCategory>>

    @Query("SELECT * FROM txn_categories WHERE id = :id")
    suspend fun getTxnCategory(id: Int): TxnCategory?
}