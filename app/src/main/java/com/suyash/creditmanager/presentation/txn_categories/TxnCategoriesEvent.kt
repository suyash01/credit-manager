package com.suyash.creditmanager.presentation.txn_categories

import com.suyash.creditmanager.domain.model.TxnCategory
import com.suyash.creditmanager.domain.util.TransactionType

sealed class TxnCategoriesEvent {
    data class CategorySelected(val txnCategory: TxnCategory): TxnCategoriesEvent()
    data class TypeSelected(val type: TransactionType): TxnCategoriesEvent()
    data class NameEntered(val name: String): TxnCategoriesEvent()
    data object UpsertCategory: TxnCategoriesEvent()
    data object DeleteCategory: TxnCategoriesEvent()
    data object BackPressed: TxnCategoriesEvent()
}