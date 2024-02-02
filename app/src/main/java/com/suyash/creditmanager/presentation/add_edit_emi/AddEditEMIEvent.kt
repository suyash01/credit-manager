package com.suyash.creditmanager.presentation.add_edit_emi

import com.suyash.creditmanager.domain.model.CreditCard
import java.time.LocalDate

sealed class AddEditEMIEvent {
    data class SelectedCard(val value: CreditCard): AddEditEMIEvent()
    data class EnteredName(val value: String): AddEditEMIEvent()
    data class EnteredAmount(val value: String): AddEditEMIEvent()
    data class EnteredRate(val value: String): AddEditEMIEvent()
    data class EnteredTaxRate(val value: String): AddEditEMIEvent()
    data class EnteredMonths(val value: String): AddEditEMIEvent()
    data class EnteredStartDate(val value: LocalDate): AddEditEMIEvent()
    data object UpsertEMI: AddEditEMIEvent()
    data object BackPressed: AddEditEMIEvent()
}