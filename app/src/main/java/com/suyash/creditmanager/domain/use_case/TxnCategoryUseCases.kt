package com.suyash.creditmanager.domain.use_case

import com.suyash.creditmanager.domain.use_case.txn_category.AddTxnCategory
import com.suyash.creditmanager.domain.use_case.txn_category.DeleteTxnCategory
import com.suyash.creditmanager.domain.use_case.txn_category.GetTxnCategories
import com.suyash.creditmanager.domain.use_case.txn_category.GetTxnCategory

data class TxnCategoryUseCases(
    val getTxnCategories: GetTxnCategories,
    val getTxnCategory: GetTxnCategory,
    val upsertTxnCategory: AddTxnCategory,
    val deleteTxnCategory: DeleteTxnCategory
)
