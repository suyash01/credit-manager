package com.suyash.creditmanager.domain.use_case.txn_category

import com.suyash.creditmanager.domain.model.TxnCategory
import com.suyash.creditmanager.domain.repository.TxnCategoryRepository

class DeleteTxnCategory(
    private val repository: TxnCategoryRepository
) {
    suspend operator fun invoke(txnCategory: TxnCategory) {
        return repository.deleteTxnCategory(txnCategory)
    }
}