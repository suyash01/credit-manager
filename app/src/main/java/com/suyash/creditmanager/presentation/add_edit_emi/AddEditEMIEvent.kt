package com.suyash.creditmanager.presentation.add_edit_emi

sealed class AddEditEMIEvent {
    data class SelectedCard(val value: Int): AddEditEMIEvent()
    data class EnteredName(val value: String): AddEditEMIEvent()
    data class EnteredAmount(val value: String): AddEditEMIEvent()
    data class EnteredRate(val value: String): AddEditEMIEvent()
    data class EnteredTaxRate(val value: String): AddEditEMIEvent()
    data class EnteredMonths(val value: String): AddEditEMIEvent()
    data class EnteredStartDate(val value: Long?): AddEditEMIEvent()
    data object UpsertEMI: AddEditEMIEvent()
    data object BackPressed: AddEditEMIEvent()
}