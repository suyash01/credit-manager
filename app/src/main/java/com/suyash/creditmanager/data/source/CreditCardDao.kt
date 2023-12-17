package com.suyash.creditmanager.data.source

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.suyash.creditmanager.domain.model.CreditCard
import kotlinx.coroutines.flow.Flow

@Dao
interface CreditCardDao {

    @Upsert
    suspend fun upsertCreditCard(creditCard: CreditCard);

    @Delete
    suspend fun deleteCreditCard(creditCard: CreditCard);

    @Query("SELECT * FROM credit_cards")
    fun getCreditCards(): Flow<List<CreditCard>>;

    @Query("SELECT * FROM credit_cards WHERE id = :id")
    suspend fun getCreditCard(id: Int): CreditCard?;
}