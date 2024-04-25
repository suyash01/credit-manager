package com.suyash.creditmanager.domain.use_case.txn_category

import com.suyash.creditmanager.domain.model.TxnCategory
import com.suyash.creditmanager.domain.repository.TxnCategoryRepository

class GetTxnCategory(
    private val repository: TxnCategoryRepository
) {
    suspend operator fun invoke(id: Int): TxnCategory? {
        return repository.getTxnCategory(id)
    }
}