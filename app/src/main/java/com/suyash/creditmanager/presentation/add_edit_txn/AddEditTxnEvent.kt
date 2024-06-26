package com.suyash.creditmanager.presentation.add_edit_txn

import com.suyash.creditmanager.domain.util.TransactionType

sealed class AddEditTxnEvent {
    data class EnteredName(val value: String): AddEditTxnEvent()
    data class SelectedCard(val value: Int): AddEditTxnEvent()
    data class SelectedTxnType(val value: TransactionType): AddEditTxnEvent()
    data class SelectedTxnCategory(val value: String): AddEditTxnEvent()
    data class EnteredAmount(val value: String): AddEditTxnEvent()
    data class EnteredDate(val value: Long?): AddEditTxnEvent()
    data object UpsertTransaction: AddEditTxnEvent()
    data object BackPressed: AddEditTxnEvent()
}
