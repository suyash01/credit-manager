package com.suyash.creditmanager.data.repository

import com.suyash.creditmanager.data.source.dao.EMIDao
import com.suyash.creditmanager.domain.model.EMI
import com.suyash.creditmanager.domain.repository.EMIRepository
import kotlinx.coroutines.flow.Flow

class EMIRepositoryImpl(private val dao: EMIDao): EMIRepository {
    override suspend fun upsertEMI(emi: EMI) {
        dao.upsertEMI(emi)
    }

    override suspend fun deleteEMI(emi: EMI) {
        dao.deleteEMI(emi)
    }

    override fun getEMIs(): Flow<List<EMI>> {
        return dao.getEMIs()
    }

    override suspend fun getEMI(id: Int): EMI? {
        return dao.getEMI(id)
    }
}