package com.suyash.creditmanager.domain.use_case

import com.suyash.creditmanager.domain.use_case.emi.AddEMI
import com.suyash.creditmanager.domain.use_case.emi.DeleteEMI
import com.suyash.creditmanager.domain.use_case.emi.GetEMI
import com.suyash.creditmanager.domain.use_case.emi.GetEMICountByCC
import com.suyash.creditmanager.domain.use_case.emi.GetEMIs
import com.suyash.creditmanager.domain.use_case.emi.GetEMIsByCC

data class EMIUseCases(
    val getEMIs: GetEMIs,
    val getEMI: GetEMI,
    val getEMIsByCC: GetEMIsByCC,
    val getEMICountByCC: GetEMICountByCC,
    val upsertEMI: AddEMI,
    val deleteEMI: DeleteEMI
)