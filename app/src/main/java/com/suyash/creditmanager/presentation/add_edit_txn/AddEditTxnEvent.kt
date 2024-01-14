package com.suyash.creditmanager.presentation.add_edit_txn

import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.util.TransactionType
import java.time.LocalDate

sealed class AddEditTxnEvent {
    data class SelectedCard(val value: CreditCard): AddEditTxnEvent()
    data class SelectedTxnType(val value: TransactionType): AddEditTxnEvent()
    data class EnteredAmount(val value: String): AddEditTxnEvent()
    data class EnteredDate(val value: LocalDate): AddEditTxnEvent()
    data object UpsertTransaction: AddEditTxnEvent()
    data object BackPressed: AddEditTxnEvent()
}
