package com.suyash.creditmanager.domain.repository

import com.suyash.creditmanager.domain.model.CreditCard
import kotlinx.coroutines.flow.Flow

interface CreditCardRepository {

    suspend fun upsertCreditCard(creditCard: CreditCard): Long

    suspend fun deleteCreditCard(creditCard: CreditCard)

    fun getCreditCards(): Flow<List<CreditCard>>

    suspend fun getCreditCard(id: Int): CreditCard?
}