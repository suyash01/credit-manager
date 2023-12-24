package com.suyash.creditmanager.data.repository

import com.suyash.creditmanager.data.source.dao.CreditCardDao
import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.repository.CreditCardRepository
import kotlinx.coroutines.flow.Flow

class CreditCardRepositoryImpl(private val dao: CreditCardDao): CreditCardRepository {
    override suspend fun upsertCreditCard(creditCard: CreditCard) {
        dao.upsertCreditCard(creditCard)
    }

    override suspend fun deleteCreditCard(creditCard: CreditCard) {
        dao.deleteCreditCard(creditCard)
    }

    override fun getCreditCards(): Flow<List<CreditCard>> {
        return dao.getCreditCards()
    }

    override suspend fun getCreditCard(id: Int): CreditCard? {
        return dao.getCreditCard(id)
    }
}