package com.suyash.creditmanager.data.source.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.suyash.creditmanager.domain.model.EMI
import kotlinx.coroutines.flow.Flow

@Dao
interface EMIDao {
    @Upsert
    suspend fun upsertEMI(emi: EMI)

    @Delete
    suspend fun deleteEMI(emi: EMI)

    @Query("SELECT * FROM emis")
    fun getEMIs(): Flow<List<EMI>>

    @Query("SELECT * FROM emis WHERE id = :id")
    suspend fun getEMI(id: Int): EMI?
}