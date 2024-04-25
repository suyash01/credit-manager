package com.suyash.creditmanager.domain.use_case.txn_category

import com.suyash.creditmanager.domain.model.TxnCategory
import com.suyash.creditmanager.domain.repository.TxnCategoryRepository
import kotlinx.coroutines.flow.Flow

class GetTxnCategories(
    private val repository: TxnCategoryRepository
) {
    operator fun invoke(): Flow<List<TxnCategory>> {
        return repository.getTxnCategories()
    }
}