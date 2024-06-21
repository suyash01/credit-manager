package com.suyash.creditmanager.domain.repository

import com.suyash.creditmanager.domain.model.EMI
import kotlinx.coroutines.flow.Flow

interface EMIRepository {

    suspend fun upsertEMI(emi: EMI)

    suspend fun deleteEMI(emi: EMI)

    fun getEMIs(): Flow<List<EMI>>

    fun getEMI(id: Int): Flow<EMI?>

    fun getEMIsByCC(id: Int): Flow<List<EMI>>

    suspend fun getEMICountByCC(id: Int): Int
}