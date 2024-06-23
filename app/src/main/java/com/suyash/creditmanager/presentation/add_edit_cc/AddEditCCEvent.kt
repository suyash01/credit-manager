package com.suyash.creditmanager.presentation.add_edit_cc

sealed class AddEditCCEvent {
    data class SelectedCardType(val value: String): AddEditCCEvent()
    data class EnteredLast4Digits(val value: String): AddEditCCEvent()
    data class EnteredCardName(val value: String): AddEditCCEvent()
    data class EnteredExpiry(val value: String): AddEditCCEvent()
    data class EnteredBillDate(val value: String): AddEditCCEvent()
    data class EnteredDueDate(val value: String): AddEditCCEvent()
    data class EnteredLimit(val value: String): AddEditCCEvent()
    data class EnteredBankName(val value: String): AddEditCCEvent()
    data class CheckedGracePeriod(val value: Boolean): AddEditCCEvent()

    data object UpsertCreditCard: AddEditCCEvent()
    data object BackPressed: AddEditCCEvent()
}
