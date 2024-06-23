package com.suyash.creditmanager.domain.use_case.credit_card

import com.suyash.creditmanager.domain.model.CreditCard
import com.suyash.creditmanager.domain.repository.CreditCardRepository
import kotlinx.coroutines.flow.Flow

class GetCreditCard(
    private val repository: CreditCardRepository
) {
    operator fun invoke(id: Int): Flow<CreditCard?> {
        return repository.getCreditCard(id)
    }
}