package com.suyash.creditmanager.data.repository

import com.suyash.creditmanager.data.source.dao.TxnCategoryDao
import com.suyash.creditmanager.domain.model.TxnCategory
import com.suyash.creditmanager.domain.repository.TxnCategoryRepository
import kotlinx.coroutines.flow.Flow

class TxnCategoryRepositoryImpl(private val dao: TxnCategoryDao): TxnCategoryRepository {
    override suspend fun upsertTxnCategory(txnCategory: TxnCategory) {
        dao.upsertTxnCategory(txnCategory)
    }

    override suspend fun deleteTxnCategory(txnCategory: TxnCategory) {
        dao.deleteTxnCategory(txnCategory)
    }

    override fun getTxnCategories(): Flow<List<TxnCategory>> {
        return dao.getTxnCategories()
    }

    override suspend fun getTxnCategory(id: Int): TxnCategory? {
        return dao.getTxnCategory(id)
    }
}