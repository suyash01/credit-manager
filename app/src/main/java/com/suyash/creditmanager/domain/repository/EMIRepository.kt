package com.suyash.creditmanager.domain.repository

import com.suyash.creditmanager.domain.model.EMI
import kotlinx.coroutines.flow.Flow

interface EMIRepository {

    suspend fun upsertEMI(emi: EMI)

    suspend fun deleteEMI(emi: EMI)

    fun getEMIs(): Flow<List<EMI>>

    suspend fun getEMI(id: Int): EMI?
}